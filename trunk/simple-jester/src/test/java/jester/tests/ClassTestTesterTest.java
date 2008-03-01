package jester.tests;

import jester.ClassTestTester;
import jester.MutationMaker;
import jester.MutationsList;
import jester.RealClassTestTester;
import jester.SourceChangeException;
import jester.TestRunner;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class ClassTestTesterTest {
	private Mockery context = new Mockery();

	@Test 
	public void changeThatFailsTestsIsNotRecorded() throws SourceChangeException {
		final TestRunner mockTestRunner = context.mock(TestRunner.class);
		context.checking(new Expectations(){{
			one(mockTestRunner).testsRunWithoutFailures(); will(returnValue(false));
		}});

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

	@Test 
	public void changeThatPassesTestsIsRecorded() throws SourceChangeException {
		final TestRunner mockTestRunner = context.mock(TestRunner.class);
		context.checking(new Expectations(){{
			one(mockTestRunner).testsRunWithoutFailures(); will(returnValue(true));
		}});

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

	@Test 
	public void originalContentsAreNotWrittenBackIfNoChanges() throws SourceChangeException {
		final TestRunner mockTestRunner = null;

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

	@Test 
	public void originalContentsAreWrittenBack() throws SourceChangeException {
		final TestRunner mockTestRunner = context.mock(TestRunner.class);
		context.checking(new Expectations(){{
			one(mockTestRunner).testsRunWithoutFailures(); will(returnValue(false));
		}});

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

	@Test 
	public void mutationListIsUsed() throws SourceChangeException {
		final TestRunner mockTestRunner = null;

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