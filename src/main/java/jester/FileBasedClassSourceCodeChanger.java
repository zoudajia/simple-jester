package jester;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileBasedClassSourceCodeChanger implements ClassSourceCodeChanger {
	private String sourceFileName;
	private IgnoreListDocument originalContents;

	private int indexOfLastChange = -1;
	private String valueChangedFrom = "not changed";
	private String valueChangedTo = "not changed";

	private Report myReport;
	private final IgnoreList ignoreList;

	public FileBasedClassSourceCodeChanger(IgnoreList ignoreList, String sourceFileName, Report aReport) {
		this.ignoreList = ignoreList;
		this.sourceFileName = sourceFileName;
		myReport = aReport;
	}

	public IgnoreListDocument getOriginalContents() throws ConfigurationException {
		try {
			if (originalContents == null) {
				originalContents = new IgnoreListDocument(Util.readFile(sourceFileName), ignoreList);
			}
			return originalContents;
		} catch (IOException ex) {
			throw new ConfigurationException(ex.getMessage());
		}
	}

	public void writeOriginalContentsBack() throws SourceChangeException {
		try {
			Writer writer = new FileWriter(sourceFileName);
			getOriginalContents().writeOnto(writer, 0, getOriginalContents().length());
			writer.close();
		} catch (IOException ex) {
			throw new SourceChangeException(ex.getMessage());
		}
	}

	public void writeOverSourceReplacing(int index, String oldContents, String newContents) throws SourceChangeException {
		indexOfLastChange = index;
		valueChangedFrom = oldContents;
		valueChangedTo = newContents;
		//
		try {
			Writer writer = new FileWriter(sourceFileName);
			getOriginalContents().writeOnto(writer, 0, index);
			writer.write(newContents, 0, newContents.length());
			int afterChangeIndex = index + oldContents.length();
			getOriginalContents().writeOnto(writer, afterChangeIndex, getOriginalContents().length() - afterChangeIndex);
			writer.close();
		} catch (IOException ex) {
			throw new SourceChangeException(ex.getMessage());
		}
	}

	public void finishJesting() throws SourceChangeException {
		myReport.finishFile(sourceFileName);
	}

	public void lastChangeCausedTestsToFail() throws SourceChangeException {
		myReport.changeThatCausedTestsToFail(indexOfLastChange, valueChangedFrom, valueChangedTo);
	}

	public void lastChangeDidNotCauseTestsToFail() throws SourceChangeException {
		myReport.changeThatDidNotCauseTestsToFail(indexOfLastChange, valueChangedFrom, valueChangedTo);
	}

	public void startJesting() throws SourceChangeException {
		myReport.startFile(sourceFileName, getOriginalContents());
	}
}