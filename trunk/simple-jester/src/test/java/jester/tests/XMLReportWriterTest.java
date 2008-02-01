package jester.tests;

import java.io.StringWriter;
import java.io.Writer;
import java.io.File;

import jester.*;
import junit.framework.*;

public class XMLReportWriterTest extends TestCase {
	public XMLReportWriterTest(String name) {
		super(name);
	}

	public void testWritingChangesAsXMLHasBothGivenAndAbsoluteFilePaths() throws SourceChangeException {
		Writer aWriter = new StringWriter();
		XMLReportWriter aXMLReportWriter = new RealXMLReportWriter(aWriter);
		String relativePathSourceFileName = "sFile";
		Object[] reportItems = new ReportItem[2];
		IgnoreListDocument originalContents = new IgnoreListDocument("once upon a time, in a land far far away ...", new IgnoreList(""));
		reportItems[0] = new ReportItem(relativePathSourceFileName, originalContents, 12, "time", "banana &&");
		reportItems[1] = new ReportItem(relativePathSourceFileName, originalContents, 23, "land", "carrot < cake > biscuit");
		int numberNotFail = 2;
		int numberChanges = 4;
		int score = 50;
		aXMLReportWriter.writeXMLReport(reportItems, relativePathSourceFileName, numberNotFail, numberChanges, score);

		String absolutePathFileName = new File(relativePathSourceFileName).getAbsolutePath();
		StringWriter expectedWriter = new StringWriter();
		expectedWriter.write("  <JestedFile fileName=\"sFile\"\r\n" + "              absolutePathFileName=\"" + absolutePathFileName + "\"\r\n"
				+ "              numberOfChangesThatDidNotCauseTestsToFail=\"2\" numberOfChanges=\"4\" score=\"50\">\r\n" + "    <ChangeThatDidNotCauseTestsToFail\r\n"
				+ "      index=\"12\" line=\"1\"\r\n" + "      from=\"time\" to=\"banana &amp;&amp;\"\r\n" + "      file=\"sFile\">\r\n"
				+ "sFile - changed source on line 1 (char index=12) from time to banana &amp;&amp;\r\n" + "once upon a >>>time, in a land far far away ...\r\n"
				+ "    </ChangeThatDidNotCauseTestsToFail>\r\n" + "    <ChangeThatDidNotCauseTestsToFail\r\n" + "      index=\"23\" line=\"1\"\r\n"
				+ "      from=\"land\" to=\"carrot &lt; cake &gt; biscuit\"\r\n" + "      file=\"sFile\">\r\n"
				+ "sFile - changed source on line 1 (char index=23) from land to carrot &lt; cake > biscuit\r\n" + "once upon a time, in a >>>land far far away ...\r\n"
				+ "    </ChangeThatDidNotCauseTestsToFail>\r\n" + "  </JestedFile>");
		String expectedXML = expectedWriter.toString();
		//
		assertEquals(Util.withoutWhitespace(expectedXML), Util.withoutWhitespace(aWriter.toString()));
	}
}