package jester.tests;

import jester.*;
import junit.framework.*;

public class TestTesterTest extends TestCase {
	public TestTesterTest(String name) {
		super(name);
	}

	public void testTestsMustStartOffWorking() {
		MockTestRunner mockTestRunner = new MockTestRunner();
		mockTestRunner.setTestsRunWithoutFailures(false);

		TestTester testTester = new TestTester(mockTestRunner, null, null);

		try {
			testTester.run();
			fail("should have stopped because tests didn't pass before making any changes");
		} catch (SourceChangeException ex) {
		}
	}

	public void testThatTestRunByIterator() throws SourceChangeException {
		MockTestRunner mockTestRunner = new MockTestRunner();
		mockTestRunner.setTestsRunWithoutFailures(true);

		ClassTestTester mockClassTestTester = new MockClassTestTester();

		MockClassIterator mockClassIterator = new MockClassIterator();
		mockClassIterator.setExpectedIterateCalls(1);
		mockClassIterator.setExpectedIterate(mockClassTestTester);

		TestTester testTester = new TestTester(mockTestRunner, mockClassIterator, mockClassTestTester);

		testTester.run();

		mockClassIterator.verify();
	}
}