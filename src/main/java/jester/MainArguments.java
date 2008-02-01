package jester;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Handling of the arguments given to the main method in TestTester.
 */
public class MainArguments {
	private static final String OPTIONAL_ARGUMENT_START = "-";
	private static final String SHOW_PROGRESS_DIALOG_OPTION_START = OPTIONAL_ARGUMENT_START + "q";

	private List directoryOrFileNames = null;
	private String buildRunningCommand = null;
	private FileExistenceChecker aFileExistenceChecker;
	private boolean shouldShowProgressDialog = true;

	public MainArguments(String[] args, FileExistenceChecker aFileExistenceChecker) throws JesterArgumentException {
		this.aFileExistenceChecker = aFileExistenceChecker;
		readArguments(args);
	}

	public static void printUsage(PrintStream out, String version) {
		out.println("Jester version " + version);
		out.println("java jester.TestTester <BuildRunningCommand> <sourceDirOrFile> <sourceDirOrFile> ...");
		out.println("example usage: java jester.TestTester \"ant\" com/xpdeveloper/server");
		out.println("for FAQ see http://jester.sourceforge.net");
		out.println("Copyright (2000-2007) Ivan Moore. Read the license.");
	}

	public String[] getDirectoryOrFileNames() {
		return (String[]) directoryOrFileNames.toArray(new String[directoryOrFileNames.size()]);
	}

	public String getBuildRunningCommand() {
		return buildRunningCommand;
	}

	public boolean shouldShowProgressDialog() {
		return shouldShowProgressDialog;
	}

	private void readArguments(String[] arguments) throws JesterArgumentException {
		List normalArgs = new ArrayList();
		List optionalArgs = new ArrayList();
		for (int i = 0; i < arguments.length; i++) {
			if (arguments[i].trim().startsWith(OPTIONAL_ARGUMENT_START)) {
				optionalArgs.add(arguments[i]);
			} else {
				normalArgs.add(arguments[i]);
			}
		}
		setBuildRunningCommandFromArguments(normalArgs);
		setDirectoriesOrFilesToMutateFromArguments(normalArgs);
		setOptionalShouldShowProgressDialogFromArguments(optionalArgs);

		if (buildRunningCommand == null)
			throw new JesterArgumentException("missing build running command argument");
		checkDirectoriesOrFilesToMutateExist();
	}

	private void checkDirectoriesOrFilesToMutateExist() throws JesterArgumentException {
		if (directoryOrFileNames == null)
			throw new JesterArgumentException("missing source directory/file name argument");
		for (Iterator name = directoryOrFileNames.iterator(); name.hasNext();) {
			String directoryOrFileName = (String) name.next();
			if (!aFileExistenceChecker.exists(directoryOrFileName)) {
				throw new JesterArgumentException("source directory/file \"" + directoryOrFileName + "\" does not exist");
			}
		}
	}

	private void setBuildRunningCommandFromArguments(List normalArgs) throws JesterArgumentException {
		if (normalArgs.size() == 0) {
			throw new JesterArgumentException("Missing Build Running Command Argument");
		}
		buildRunningCommand = (String) normalArgs.get(0);
	}

	private void setDirectoriesOrFilesToMutateFromArguments(List normalArgs) throws JesterArgumentException {
		if (normalArgs.size() < 2) {
			throw new JesterArgumentException("Missing Directories Or Files To Mutate Arguments");
		}
		directoryOrFileNames = new ArrayList();
		directoryOrFileNames.addAll(normalArgs);
		directoryOrFileNames.remove(0);
	}

	private void setOptionalShouldShowProgressDialogFromArguments(List optionalArgs) {
		for (Iterator args = optionalArgs.iterator(); args.hasNext();) {
			String arg = (String) args.next();
			if (arg.startsWith(SHOW_PROGRESS_DIALOG_OPTION_START)) {
				shouldShowProgressDialog = false;
				return;
			}
		}
	}
}