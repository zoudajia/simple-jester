package jester.tests;

import jester.ClassTestTester;
import jester.MutationsList;
import jester.RealClassTestTester;
import jester.SourceChangeException;
import jester.TestRunner;
import junit.framework.TestCase;

public class ClassTestTesterTest extends TestCase {
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

	private ClassTestTester newRealClassTestTester(TestRunner aTestRunner) {
		MutationsList aMutationsList = new MockMutationsList();
		return new RealClassTestTester(aTestRunner, aMutationsList);
	}

	public void testThatMutationListIsUsed() throws SourceChangeException {
		MockTestRunner mockTestRunner = new MockTestRunner();

		MockClassSourceChanger mockClassSourceCodeChanger = new MockClassSourceChanger();
		mockClassSourceCodeChanger.setOriginalContents("includes nothing that will be changed");

		MockMutationsList aMockMutationsList = new MockMutationsList();
		aMockMutationsList.setExpectedVisitCalls(1);

		ClassTestTester classTestTester = new RealClassTestTester(mockTestRunner, aMockMutationsList);

		classTestTester.testUsing(mockClassSourceCodeChanger);

		aMockMutationsList.verify();
	}
}