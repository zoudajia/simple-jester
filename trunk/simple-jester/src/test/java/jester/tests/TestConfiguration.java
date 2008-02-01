package jester.tests;

import jester.Configuration;
import jester.Logger;

public class TestConfiguration implements Configuration {
	public TestConfiguration() {
		super();
	}

	public boolean shouldReportEagerly() {
		return false;
	}

	public String sourceFileExtension() {
		return ".java";
	}

	public String buildRunningCommand() {
		unused();
		return null;
	}

	public String buildPassString() {
		unused();
		return null;
	}

	public Logger getLogger() {
		unused();
		return null;
	}

	private void unused() {
		throw new RuntimeException("TestConfiguration used for something it wasn't expecting to be used for.");
	}

	public String xmlReportFileName() {
		unused();
		return null;
	}

	public boolean closeUIOnFinish() {
		return false;
	}
}