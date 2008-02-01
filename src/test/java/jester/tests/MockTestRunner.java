package jester.tests;

import jester.TestRunner;

public class MockTestRunner implements TestRunner {
	private boolean testsRunWithoutFailures = false;

	public void setTestsRunWithoutFailures(boolean b) {
		testsRunWithoutFailures = b;
	}

	public boolean testsRunWithoutFailures() {
		return testsRunWithoutFailures;
	}
}