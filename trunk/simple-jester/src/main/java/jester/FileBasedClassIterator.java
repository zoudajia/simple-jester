package jester;

import java.io.File;
import java.util.List;

public class FileBasedClassIterator implements ClassIterator {
	private Configuration myConfiguration;
	private List<String> myFileNames;
	private Report myReport;
	private final IgnoreList ignoreList;

	public FileBasedClassIterator(Configuration configuration, IgnoreList ignoreList, List<String> directoryNames, Report aReport) {
		myConfiguration = configuration;
		this.ignoreList = ignoreList;
		myFileNames = directoryNames;
		myReport = aReport;
	}

	public void iterate(final ClassTestTester visitor) throws SourceChangeException {
		final int[] numberOfFiles = new int[1];
		numberOfFiles[0] = 0;
		FileVisitor fileCounter = new FileVisitor() {
			public void visit(String fileName) {
				if (fileName.endsWith(myConfiguration.sourceFileExtension())) {
					numberOfFiles[0]++;
				}
			}
		};
		for (String fileName : myFileNames) {
			visitFileOrDirectory(fileName, fileCounter);
		}
		myReport.setNumberOfFilesThatWillBeTested(numberOfFiles[0]);

		FileVisitor classTestVisitorWrapper = new FileVisitor() {
			public void visit(String fileName) throws SourceChangeException {
				if (fileName.endsWith(myConfiguration.sourceFileExtension())) {
					ClassSourceCodeChanger sourceCodeSystem = new FileBasedClassSourceCodeChanger(ignoreList, fileName, myReport);
					visitor.testUsing(sourceCodeSystem);
				}
			}
		};

		for (String fileName : myFileNames) {
			visitFileOrDirectory(fileName, classTestVisitorWrapper);
		}
	}

	private void visitFileOrDirectory(String fileName, FileVisitor visitor) throws SourceChangeException {
		File file = new File(fileName);
		if (file.isDirectory()) {
			iterateForFilesInDirectory(file, visitor);
		} else {
			visitor.visit(fileName);
		}
	}

	private void iterateForFilesInDirectory(File directory, FileVisitor visitor) throws SourceChangeException {
		for (String fileNameWithoutPath : directory.list()) {
			String fileName = directory.getPath() + File.separator + fileNameWithoutPath;
			visitFileOrDirectory(fileName, visitor);
		}
	}
}