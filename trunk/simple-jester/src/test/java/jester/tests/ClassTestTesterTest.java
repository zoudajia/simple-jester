package jester.tests;

import org.jmock.Expectations;
import org.jmock.Mockery;

import jester.ClassTestTester;
import jester.MutationMaker;
import jester.MutationsList;
import jester.RealClassTestTester;
import jester.SourceChangeException;
import jester.TestRunner;
import junit.framework.TestCase;

public class ClassTestTesterTest extends TestCase {
	private Mockery context = new Mockery();

	public ClassTestTesterTest(String name) {
		super(name);
	}

	public void testThatChangeThatFailsTestsIsNotRecorded() throws SourceChangeException {
		MockTestRunner mockTestRunner = new MockTestRunner();
		mockTestRunner.setTestsRunWithoutFailures(false);

		MockClassSourceChanger mockClassSourceCodeChanger = new MockClassSourceChanger();
		mockClassSourceCodeChanger.setOriginalContents("includes 1 thing that will be changed");
		mockClassSourceCodeChanger.setExpectedWriteOverSourceReplacing(9, "1", "2");
		mockClassSourceCodeChanger.setExpectedWriteOverSourceReplacingCalls(1);
		mockClassSourceCodeChanger.setExpectedWriteOriginalBackCalls(1);
		mockClassSourceCodeChanger.setExpectedStartJestingCalls(1);
		mockClassSourceCodeChanger.setExpectedFinishJestingCalls(1);

		mockClassSourceCodeChanger.setExpectedLastChangeDidNotCauseTestsToFailCalls(0);
		mockClassSourceCodeChanger.setExpectedLastChangeCausedTestsToFailCalls(1);

		ClassTestTester classTestTester = newRealClassTestTester(mockTestRunner);

		classTestTester.testUsing(mockClassSourceCodeChanger);

		mockClassSourceCodeChanger.verify();
	}

	public void testThatChangeThatPassesTestsIsRecorded() throws SourceChangeException {
		MockTestRunner mockTestRunner = new MockTestRunner();
		mockTestRunner.setTestsRunWithoutFailures(true);

		MockClassSourceChanger mockClassSourceCodeChanger = new MockClassSourceChanger();
		mockClassSourceCodeChanger.setOriginalContents("includes 1 thing that will be changed");
		mockClassSourceCodeChanger.setExpectedWriteOverSourceReplacing(9, "1", "2");
		mockClassSourceCodeChanger.setExpectedWriteOverSourceReplacingCalls(1);
		mockClassSourceCodeChanger.setExpectedWriteOriginalBackCalls(1);
		mockClassSourceCodeChanger.setExpectedStartJestingCalls(1);
		mockClassSourceCodeChanger.setExpectedFinishJestingCalls(1);

		mockClassSourceCodeChanger.setExpectedLastChangeDidNotCauseTestsToFailCalls(1);
		mockClassSourceCodeChanger.setExpectedLastChangeCausedTestsToFailCalls(0);

		ClassTestTester classTestTester = newRealClassTestTester(mockTestRunner);

		classTestTester.testUsing(mockClassSourceCodeChanger);

		mockClassSourceCodeChanger.verify();
	}

	public void testThatOriginalContentsAreNotWrittenBackIfNoChanges() throws SourceChangeException {
		MockTestRunner mockTestRunner = new MockTestRunner();

		MockClassSourceChanger mockClassSourceCodeChanger = new MockClassSourceChanger();
		mockClassSourceCodeChanger.setOriginalContents("includes nothing that will be changed");
		mockClassSourceCodeChanger.setExpectedWriteOverSourceReplacingCalls(0);
		mockClassSourceCodeChanger.setExpectedWriteOriginalBackCalls(0);
		mockClassSourceCodeChanger.setExpectedStartJestingCalls(1);
		mockClassSourceCodeChanger.setExpectedFinishJestingCalls(1);

		mockClassSourceCodeChanger.setExpectedLastChangeDidNotCauseTestsToFailCalls(0);
		mockClassSourceCodeChanger.setExpectedLastChangeCausedTestsToFailCalls(0);

		ClassTestTester classTestTester = newRealClassTestTester(mockTestRunner);

		classTestTester.testUsing(mockClassSourceCodeChanger);

		mockClassSourceCodeChanger.verify();
	}

	public void testThatOriginalContentsAreWrittenBack() throws SourceChangeException {
		MockTestRunner mockTestRunner = new MockTestRunner();

		MockClassSourceChanger mockClassSourceCodeChanger = new MockClassSourceChanger();
		mockClassSourceCodeChanger.setOriginalContents("includes 1 thing that will be changed");
		mockClassSourceCodeChanger.setExpectedWriteOverSourceReplacing(9, "1", "2");
		mockClassSourceCodeChanger.setExpectedWriteOverSourceReplacingCalls(1);
		mockClassSourceCodeChanger.setExpectedWriteOriginalBackCalls(1);
		mockClassSourceCodeChanger.setExpectedStartJestingCalls(1);
		mockClassSourceCodeChanger.setExpectedFinishJestingCalls(1);

		mockClassSourceCodeChanger.setExpectedLastChangeDidNotCauseTestsToFailCalls(0);
		mockClassSourceCodeChanger.setExpectedLastChangeCausedTestsToFailCalls(1);

		ClassTestTester classTestTester = newRealClassTestTester(mockTestRunner);

		classTestTester.testUsing(mockClassSourceCodeChanger);

		mockClassSourceCodeChanger.verify();
	}

	private ClassTestTester newRealClassTestTester(TestRunner aTestRunner) throws SourceChangeException {
		final MutationsList mockMutationsList = context.mock(MutationsList.class);
		context.checking(new Expectations(){{
			allowing(mockMutationsList).visit(with(any(MutationMaker.class)));
		}});
		return new RealClassTestTester(aTestRunner, mockMutationsList);
	}

	public void testThatMutationListIsUsed() throws SourceChangeException {
		MockTestRunner mockTestRunner = new MockTestRunner();

		MockClassSourceChanger mockClassSourceCodeChanger = new MockClassSourceChanger();
		mockClassSourceCodeChanger.setOriginalContents("includes nothing that will be changed");

		final MutationsList mockMutationsList = context.mock(MutationsList.class);
		context.checking(new Expectations(){{
			one(mockMutationsList).visit(with(any(MutationMaker.class)));
		}});

		ClassTestTester classTestTester = new RealClassTestTester(mockTestRunner, mockMutationsList);

		classTestTester.testUsing(mockClassSourceCodeChanger);

		context.assertIsSatisfied();
	}
}