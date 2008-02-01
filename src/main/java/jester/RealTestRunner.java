package jester;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

public class RealTestRunner implements TestRunner {
	private Configuration myConfiguration;
	private String myBuildRunningCommand;

	public static void main(String args[]) throws IOException, SourceChangeException {
		System.out.println(new RealTestRunner(new RealConfiguration(RealConfiguration.DEFAULT_CONFIGURATION_FILENAME), args[0]).testsRunWithoutFailures());
	}

	public boolean testsRunWithoutFailures() throws SourceChangeException {
		try {
			Vector output = Util.runCommand(myBuildRunningCommand, myConfiguration.getLogger());
			boolean passed = contains(output, myConfiguration.buildPassString());
			return passed;
		} catch (IOException e) {
			throw new SourceChangeException("couldn't run tests", e);
		}
	}

	private boolean contains(Vector output, String string) {
		for (Iterator iter = output.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if (element.contains(string)) {
				return true;
			}
		}
		return false;
	}

	public RealTestRunner(Configuration aConfiguration, String buildRunningCommand) {
		myConfiguration = aConfiguration;
		myBuildRunningCommand = buildRunningCommand;
	}
}