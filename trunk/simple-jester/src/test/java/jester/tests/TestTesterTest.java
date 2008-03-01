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
	private Mockery context = new Mockery();
	
	public TestTesterTest(String name) {
		super(name);
	}

	public void testTestsMustStartOffWorking() throws SourceChangeException {
		final TestRunner mockTestRunner = context.mock(TestRunner.class);
		context.checking(new Expectations(){{
			one(mockTestRunner).testsRunWithoutFailures(); will(returnValue(false));
		}});

		TestTester testTester = new TestTester(mockTestRunner, null, null);

		try {
			testTester.run();
			fail("should have stopped because tests didn't pass before making any changes");
		} catch (SourceChangeException ex) {
		}
	}

	public void testThatTestRunByIterator() throws SourceChangeException {
		final TestRunner mockTestRunner = context.mock(TestRunner.class);
		final ClassTestTester mockClassTestTester = context.mock(ClassTestTester.class);
		final ClassIterator mockClassIterator = context.mock(ClassIterator.class);
		
		context.checking(new Expectations() {{
			one (mockTestRunner).testsRunWithoutFailures(); will(returnValue(true));
		    one (mockClassIterator).iterate(mockClassTestTester);
		}});

		TestTester testTester = new TestTester(mockTestRunner, mockClassIterator, mockClassTestTester);

		testTester.run();
		
		context.assertIsSatisfied();
	}
}