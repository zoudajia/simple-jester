package jester.tests;

import jester.Configuration;
import jester.Logger;
import jester.RealLogger;

public class MockConfiguration implements Configuration {

	private String myCompilationCommand = "javac";
	private Logger logger = new RealLogger();

	public void setCompilationCommand(String cmd) {
		myCompilationCommand = cmd;
	}

	public String compilationCommand() {
		return myCompilationCommand;
	}

	public Logger getLogger() {
		return logger;
	}

	public boolean shouldReportEagerly() {
		return true;
	}

	public String sourceFileExtension() {
		return ".java";
	}

	public String buildRunningCommand() {
		return "whatever";
	}

	public String buildPassString() {
		return "PASSED";
	}

	public String xmlReportFileName() {
		return "jesterReport.xml";
	}

	public boolean closeUIOnFinish() {
		return false;
	}

}
