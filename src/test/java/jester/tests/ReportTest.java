package jester.tests;

import java.io.PrintWriter;
import java.io.StringWriter;

import jester.ConfigurationException;
import jester.IgnoreList;
import jester.IgnoreListDocument;
import jester.RealReport;
import jester.Report;
import jester.SourceChangeException;
import jester.XMLReportWriter;
import junit.framework.TestCase;

public class ReportTest extends TestCase {
	private XMLReportWriter myXMLWriter = new MockXMLReportWriter();

	public ReportTest(String name) {
		super(name);
	}

	private Report newRealReport() {
		return newRealReport(new PrintWriter(new StringWriter()));
	}

	private Report newRealReport(PrintWriter aPrintWriter) {
		return new RealReport(new TestConfiguration(), aPrintWriter, myXMLWriter, new MockProgressReporter());
	}

	private void startFile(Report aReport, String fileName, String originalContents) throws ConfigurationException, SourceChangeException {
		aReport.startFile(fileName, new IgnoreListDocument(originalContents, new IgnoreList("")));
	}

	public void testBottomScore() throws SourceChangeException {
		Report aReport = newRealReport();
		startFile(aReport, "sourceFileName", "originalContents");
		int indexOfChange = 7;
		aReport.changeThatDidNotCauseTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		assertEquals(0, aReport.totalScore());
		assertEquals(0, aReport.fileScore());
	}

	public void testCantFinishDifferentFile() throws SourceChangeException {
		Report aReport = newRealReport();
		startFile(aReport, "sourceFileName", "originalContents");
		try {
			aReport.finishFile("otherFile");
			fail("can't finish different file - should have thrown exception");
		} catch (SourceChangeException ex) {
		}
	}

	public void testCantStartTwoFiles() throws SourceChangeException {
		Report aReport = newRealReport();
		startFile(aReport, "sourceFileName", "originalContents");
		try {
			startFile(aReport, "otherFile", "originalContents");
			fail("can't start file if haven't finished other - should have thrown exception");
		} catch (SourceChangeException ex) {
		}
	}

	public void testChangesToSameFileOrderedByIndex() throws SourceChangeException {
		StringWriter sw = new StringWriter();
		Report aReport = newRealReport(new PrintWriter(sw));
		startFile(aReport, "sourceFileName1", "theOriginalContentsBeforeChanging");
		{
			int indexOfChange4 = 7;
			aReport.changeThatDidNotCauseTestsToFail(indexOfChange4, "valueChangedFrom4", "valueChangedTo4");
		}
		{
			int indexOfChange1 = 3;
			aReport.changeThatDidNotCauseTestsToFail(indexOfChange1, "valueChangedFrom1", "valueChangedTo1");
		}
		{
			int indexOfChange3 = 6;
			aReport.changeThatDidNotCauseTestsToFail(indexOfChange3, "valueChangedFrom3", "valueChangedTo3");
		}
		{
			int indexOfChange2 = 5;
			aReport.changeThatDidNotCauseTestsToFail(indexOfChange2, "valueChangedFrom2", "valueChangedTo2");
		}
		aReport.finishFile("sourceFileName1");
		String reportString = sw.toString();
		int indexOfC1 = reportString.indexOf("valueChangedFrom1");
		int indexOfC2 = reportString.indexOf("valueChangedFrom2");
		int indexOfC3 = reportString.indexOf("valueChangedFrom3");
		int indexOfC4 = reportString.indexOf("valueChangedFrom4");
		assertTrue("change 1 was recorded", indexOfC1 != -1);
		assertTrue("change 2 was recorded", indexOfC2 != -1);
		assertTrue("change 3 was recorded", indexOfC3 != -1);
		assertTrue("change 4 was recorded", indexOfC4 != -1);
		assertTrue("changes to file 1 are sorted", indexOfC1 < indexOfC2);
		assertTrue("changes to file 1 are sorted", indexOfC2 < indexOfC3);
		assertTrue("changes to file 1 are sorted", indexOfC3 < indexOfC4);
	}

	public void testFileScoreSeparateFromTotalScore() throws SourceChangeException {
		Report aReport = newRealReport();
		startFile(aReport, "sourceFileName", "originalContents");
		{
			int indexOfChange = 5;
			aReport.changeThatCausedTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		}
		{
			int indexOfChange = 7;
			aReport.changeThatDidNotCauseTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		}
		aReport.finishFile("sourceFileName");
		startFile(aReport, "otherFile", "originalContents");
		assertEquals(50, aReport.totalScore());
		assertEquals(Report.INITIAL_SCORE, aReport.fileScore());
	}

	public void testInitialScore() throws SourceChangeException {
		Report aReport = newRealReport();
		assertEquals(Report.INITIAL_SCORE, aReport.totalScore());
		assertEquals(Report.INITIAL_SCORE, aReport.fileScore());

		aReport = newRealReport();
		startFile(aReport, "sourceFileName", "originalContents");
		assertEquals(Report.INITIAL_SCORE, aReport.totalScore());
		assertEquals(Report.INITIAL_SCORE, aReport.fileScore());
	}

	public void testMediumScore() throws SourceChangeException {
		Report aReport = newRealReport();
		startFile(aReport, "sourceFileName", "originalContents");
		{
			int indexOfChange = 5;
			aReport.changeThatCausedTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		}
		{
			int indexOfChange = 7;
			aReport.changeThatDidNotCauseTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		}
		assertEquals(50, aReport.totalScore());
		assertEquals(50, aReport.fileScore());
	}

	public void testThatChangeThatCausedTestsToFailIsNotIncluded() throws SourceChangeException {
		StringWriter sw = new StringWriter();
		Report aReport = newRealReport(new PrintWriter(sw));
		startFile(aReport, "sourceFileName", "originalContents");

		int indexOfChange = 5;
		aReport.changeThatCausedTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		aReport.finishFile("sourceFileName");
		String reportString = sw.toString();
		assertTrue("report should include interesting stuff1", reportString.indexOf("sourceFileName") != -1);
		assertTrue("report should not include uninteresting stuff2", reportString.indexOf("valueChangedFrom") == -1);
		assertTrue("report should not include uninteresting stuff3", reportString.indexOf("valueChangedTo") == -1);
		assertTrue("report should not include uninteresting stuff4", reportString.indexOf(Integer.toString(indexOfChange)) == -1);
	}

	public void testThatChangeThatDidNotCauseTestsToFailIsIncluded() throws SourceChangeException {
		StringWriter sw = new StringWriter();
		Report aReport = newRealReport(new PrintWriter(sw));
		startFile(aReport, "sourceFileName", "theOriginalContentsBeforeChanging");

		int indexOfChange = 5;
		aReport.changeThatDidNotCauseTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		aReport.finishFile("sourceFileName");
		String reportString = sw.toString();
		assertTrue("report should include interesting stuff1", reportString.indexOf("sourceFileName") != -1);
		assertTrue("report should include interesting stuff2", reportString.indexOf("valueChangedFrom") != -1);
		assertTrue("report should include interesting stuff3", reportString.indexOf("valueChangedTo") != -1);
		assertTrue("report should include interesting stuff4", reportString.indexOf(Integer.toString(indexOfChange)) != -1);
	}

	public void testTopScore() throws SourceChangeException {
		Report aReport = newRealReport();
		startFile(aReport, "sourceFileName", "originalContents");
		int indexOfChange = 5;
		aReport.changeThatCausedTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		assertEquals(100, aReport.totalScore());
		assertEquals(100, aReport.fileScore());
	}

	public void testTotalScoreForManyFiles() throws SourceChangeException {
		Report aReport = newRealReport();
		//
		startFile(aReport, "sourceFileName1", "theOriginalContentsBeforeChanging");
		{
			int indexOfChange = 5;
			aReport.changeThatCausedTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		}
		{
			int indexOfChange = 7;
			aReport.changeThatDidNotCauseTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		}
		assertEquals(50, aReport.fileScore());
		aReport.finishFile("sourceFileName1");
		//
		startFile(aReport, "sourceFileName2", "theOriginalContentsBeforeChanging");
		{
			int indexOfChange = 5;
			aReport.changeThatDidNotCauseTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		}
		{
			int indexOfChange = 7;
			aReport.changeThatCausedTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		}
		{
			int indexOfChange = 9;
			aReport.changeThatDidNotCauseTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
		}
		assertEquals(34, aReport.fileScore());
		aReport.finishFile("sourceFileName2");
		//
		assertEquals(40, aReport.totalScore());
	}

	public void testFileMustBeStarted() {
		Report aReport = newRealReport();
		{
			try {
				int indexOfChange = 5;
				aReport.changeThatCausedTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
				fail("can't report on file haven't started (changeThatCausedTestsToFail) - should have thrown exception");
			} catch (SourceChangeException ex) {
			}
		}
		{
			try {
				int indexOfChange = 7;
				aReport.changeThatDidNotCauseTestsToFail(indexOfChange, "valueChangedFrom", "valueChangedTo");
				fail("can't report on file haven't started (changeThatDidNotCauseTestsToFail) - should have thrown exception");
			} catch (SourceChangeException ex) {
			}
		}
	}
}