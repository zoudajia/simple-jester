package jester;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handling of the arguments given to the main method in TestTester.
 */
public class MainArguments {
	private static final String BUILD_COMMAND_ARG_NAME = "buildCommand";
	private static final String SOURCE_ARG_NAME = "source";
	private static final String CONFIG_ARG_NAME = "config";
	private static final String IGNORE_ARG_NAME = "ignore";
	private static final String MUTATIONS_ARG_NAME = "mutations";

	private final FileExistenceChecker aFileExistenceChecker;
	private final String[] args;
	private final Map<String, List<String>> argMap = new HashMap<String, List<String>>();

	public MainArguments(String[] args, FileExistenceChecker aFileExistenceChecker) throws JesterArgumentException {
		this.args = args;
		this.aFileExistenceChecker = aFileExistenceChecker;
		parse();
		checkArgs();
	}

	private void checkArgs() throws JesterArgumentException {
		checkManditoryFieldsExist(BUILD_COMMAND_ARG_NAME, SOURCE_ARG_NAME);
		checkNotMoreThanOneValueExists(BUILD_COMMAND_ARG_NAME, IGNORE_ARG_NAME);
		checkFilesExist(SOURCE_ARG_NAME, IGNORE_ARG_NAME);
	}

	private void checkFilesExist(String... argNames) throws JesterArgumentException {
		for (String argName : argNames) {
			List<String> fileNames = get(argName);
			if (fileNames != null) {
				for (String fileName : fileNames) {
					if (!aFileExistenceChecker.exists(fileName)) {
						throw new JesterArgumentException("Source file or directory <" + fileName + "> doesn't exist.");
					}
				}
			}
		}
	}

	private void checkNotMoreThanOneValueExists(String... argNames) throws JesterArgumentException {
		for (String argName : argNames) {
			if (get(argName) != null && get(argName).size() != 1) {
				throw new JesterArgumentException("Can only have one <" + argName + ">");
			}
		}
	}

	private void checkManditoryFieldsExist(String... argNames) throws JesterArgumentException {
		for (String argName : argNames) {
			if (get(argName) == null) {
				throw new JesterArgumentException("Didn't have manditory argument <" + argName + ">");
			}
		}
	}

	private void parse() throws JesterArgumentException {
		String name = null;
		for (String arg : args) {
			boolean isName = arg.startsWith("-");
			if (isName) {
				name = arg.substring(1);
			} else {
				if (name == null) {
					throw new JesterArgumentException("Badly formed arguments");
				}
				append(argMap, name, arg);
			}
		}
	}

	private void append(Map<String, List<String>> argMap, String name, String arg) {
		List<String> values = get(name);
		if (values == null) {
			values = new ArrayList<String>();
			argMap.put(name, values);
		}
		values.add(arg);
	}

	public static void printUsage(PrintStream out, String version) {
		out.println("Jester version " + version);
		String optionaArguments = "[-" + MUTATIONS_ARG_NAME + " foo.txt -" + IGNORE_ARG_NAME + " bar.txt -" + CONFIG_ARG_NAME + " j.txt]";
		String manditoryArguments = "-" + BUILD_COMMAND_ARG_NAME + " <BuildRunningCommand> -" + SOURCE_ARG_NAME + " <sourceDirOrFile> <sourceDirOrFile> ... ";
		out.println("java -jar simple-jester.jar " + manditoryArguments + optionaArguments);
		out.println("example usage: java -jar simple-jester.jar \"ant\" com/oocode/foo");
		out.println("for FAQ see http://jester.sourceforge.net");
		out.println("Copyright (2000-2008) Ivan Moore. Read the license.");
	}

	public List<String> getDirectoryOrFileNames() {
		return get(SOURCE_ARG_NAME);
	}

	public String getBuildRunningCommand() {
		return getBuildRunningCommands().get(0);
	}

	private List<String> getBuildRunningCommands() {
		return get(BUILD_COMMAND_ARG_NAME);
	}

	public boolean shouldShowProgressDialog() {
		return false;
	}

	public String getIgnoreListFileName() {
		return getIgnoreListFileNames().get(0);
	}

	private List<String> getIgnoreListFileNames() {
		return get(IGNORE_ARG_NAME);
	}

	private List<String> get(String argName) {
		return argMap.get(argName);
	}
}