package jester.tests;

import jester.ClassIterator;
import jester.ClassTestTester;
import jester.SourceChangeException;
import jester.TestRunner;
import jester.TestTester;
import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

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
		Mockery context = new Mockery();
		final TestRunner mockTestRunner = context.mock(TestRunner.class);
		context.checking(new Expectations() {{
		    one (mockTestRunner).testsRunWithoutFailures(); will(returnValue(true));
		}});

		final ClassTestTester mockClassTestTester = context.mock(ClassTestTester.class);

		final ClassIterator mockClassIterator = context.mock(ClassIterator.class);
		context.checking(new Expectations() {{
		    one (mockClassIterator).iterate(mockClassTestTester);
		}});

		TestTester testTester = new TestTester(mockTestRunner, mockClassIterator, mockClassTestTester);

		testTester.run();
		
		context.assertIsSatisfied();
	}
}