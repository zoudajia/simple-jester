package jester.tests;

import jester.*;
import junit.framework.Assert;

public class MockClassSourceChanger implements ClassSourceCodeChanger {
	private IgnoreListDocument originalContents;
	private boolean throwExceptionOnFirstChangeAttempt = false;

	private int expectedIndex;
	private String expectedOldContents;
	private String expectedNewContents;

	private int actualWriteOverSourceReplacingCalls = 0;
	private int expectedWriteOverSourceReplacingCalls = 0;

	private int actualWriteOriginalBackCalls = 0;
	private int expectedWriteOriginalBackCalls = 0;

	private int actualReportLastChangeCalls = 0;
	private int expectedReportLastChangeCalls = 0;
	private int expectedLastChangeCausedTestsToFailCalls = 0;
	private int actualLastChangeCausedTestsToFailCalls = 0;
	public MockClassSourceChanger() {
		super();
	}
	public IgnoreListDocument getOriginalContents() {
		return originalContents;
	}

	public void setExpectedLastChangeCausedTestsToFailCalls(int calls) {
		expectedLastChangeCausedTestsToFailCalls = calls;
	}
	public void setExpectedLastChangeDidNotCauseTestsToFailCalls(int calls) {
		expectedReportLastChangeCalls = calls;
	}
	public void setExpectedWriteOriginalBackCalls(int calls) {
		expectedWriteOriginalBackCalls = calls;
	}
	public void setExpectedWriteOverSourceReplacing(int index, String oldContents, String newContents) {
		expectedIndex = index;
		expectedOldContents = oldContents;
		expectedNewContents = newContents;
	}
	public void setExpectedWriteOverSourceReplacingCalls(int calls) {
		expectedWriteOverSourceReplacingCalls = calls;
	}
	public void setOriginalContents(IgnoreListDocument contents) {
		originalContents = contents;
	}
	public void setOriginalContents(String contents){
		setOriginalContents(contents, new IgnoreList(""));
	}
	public void setOriginalContents(String contents, IgnoreList anIgnoreList) {
		try {
			originalContents = new IgnoreListDocument(contents, anIgnoreList);
		} catch (ConfigurationException e) {
			throw new RuntimeException("test setup failed doing MockClassSourceChanger.setOriginalContents");
		}
	}
	public void setThrowExceptionOnFirstChangeAttempt(boolean throwExceptionOnFirstChangeAttempt) {
		this.throwExceptionOnFirstChangeAttempt = throwExceptionOnFirstChangeAttempt;
	}
	public void verify() {
		Assert.assertEquals("WriteOverSourceReplacingCalls", expectedWriteOverSourceReplacingCalls, actualWriteOverSourceReplacingCalls);
		Assert.assertEquals("WriteOriginalBackCalls", expectedWriteOriginalBackCalls, actualWriteOriginalBackCalls);
		Assert.assertEquals("ReportLastChangeCalls", expectedReportLastChangeCalls, actualReportLastChangeCalls);
		Assert.assertEquals("ReportLastChangeCalls", expectedLastChangeCausedTestsToFailCalls, actualLastChangeCausedTestsToFailCalls);
		Assert.assertEquals("StartJestingCalls", expectedStartJestingCalls, actualStartJestingCalls);
		Assert.assertEquals("FinishJestingCalls", expectedFinishJestingCalls, actualFinishJestingCalls);
	}
	public void writeOriginalContentsBack() {
		actualWriteOriginalBackCalls++;
	}
	public void writeOverSourceReplacing(int index, String oldContents, String newContents) throws SourceChangeException {
		actualWriteOverSourceReplacingCalls++;
		if (throwExceptionOnFirstChangeAttempt) {
			throwExceptionOnFirstChangeAttempt = false;
			throw new SourceChangeException("mock threw exception");
		}
		Assert.assertEquals(expectedIndex, index);
		Assert.assertEquals(expectedOldContents, oldContents);
		Assert.assertEquals(expectedNewContents, newContents);
	}

	private int actualFinishJestingCalls = 0;
	private int actualStartJestingCalls = 0;
	private int expectedFinishJestingCalls = 0;
	private int expectedStartJestingCalls = 0;

	public void finishJesting() {
		actualFinishJestingCalls++;
	}

	public void lastChangeCausedTestsToFail() {
		actualLastChangeCausedTestsToFailCalls++;
	}

	public void lastChangeDidNotCauseTestsToFail() {
		actualReportLastChangeCalls++;
	}

	public void setExpectedFinishJestingCalls(int calls) {
		expectedFinishJestingCalls = calls;
	}

	public void setExpectedStartJestingCalls(int calls) {
		expectedStartJestingCalls = calls;
	}

	public void startJesting() {
		actualStartJestingCalls++;
	}
}