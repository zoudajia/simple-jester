package jester.tests;

import jester.TestRunner;

public class MockTestRunner implements TestRunner {
	private boolean testsRunWithoutFailures = false;

	public MockTestRunner() {
		super();
	}

	public void setTestsRunWithoutFailures(boolean b) {
		testsRunWithoutFailures = b;
	}

	public boolean testsRunWithoutFailures() {
		return testsRunWithoutFailures;
	}
}