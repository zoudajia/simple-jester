package jester.tests;

import jester.ReportItem;
import jester.XMLReportWriter;

public class MockXMLReportWriter implements XMLReportWriter {

	public void writeXMLReport(ReportItem[] reportItems, String sourceFileName, int numberOfChangesThatDidNotCauseTestsToFail, int numberOfChanges, int score) {
	}

}
