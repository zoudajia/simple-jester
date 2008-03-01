package jester;

public interface XMLReportWriter {

	void writeXMLReport(ReportItem[] reportItems, String sourceFileName, int numberOfChangesThatDidNotCauseTestsToFail, int numberOfChanges, int score) throws SourceChangeException;

}
