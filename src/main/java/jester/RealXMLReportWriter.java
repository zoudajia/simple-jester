package jester;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

public class RealXMLReportWriter implements XMLReportWriter {

	private Writer myWriter;

	public RealXMLReportWriter(Writer aWriter) {
		myWriter = aWriter;
	}

	public void writeXMLReport(ReportItem[] reportItems, String sourceFileName, int numberOfChangesThatDidNotCauseTestsToFail, int numberOfChanges, int score)
			throws SourceChangeException {

		try {
			String absoluteFilePath = new File(sourceFileName).getAbsolutePath();
			myWriter.write("  <JestedFile fileName=\"" + sourceFileName + "\"\n              absolutePathFileName=\"" + absoluteFilePath
					+ "\"\n              numberOfChangesThatDidNotCauseTestsToFail=\"" + numberOfChangesThatDidNotCauseTestsToFail + "\" numberOfChanges=\"" + numberOfChanges
					+ "\" score=\"" + score + "\">\n");
			for (ReportItem aReportItem : reportItems) {
				myWriter.write(aReportItem.asXML() + "\n");
			}
			myWriter.write("  </JestedFile>\n");
		} catch (IOException ex) {
			throw new SourceChangeException(ex.toString());
		}
	}
}
