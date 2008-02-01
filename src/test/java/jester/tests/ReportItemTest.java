package jester.tests;

import jester.*;
import jester.ReportItem;
import jester.SourceChangeException;
import junit.framework.*;

public class ReportItemTest extends TestCase {
	public ReportItemTest(String name) {
		super(name);
	}

	public static void main(String args[]) {
		junit.awtui.TestRunner.main(new String[] { "jester.tests.ReportItemTest" });
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReportItemTest.class);
		return suite;
	}

	private ReportItem newReportItem(String originalContents, String from, String to, int indexOfChange) throws ConfigurationException {
		return new ReportItem("sourceFileName", new IgnoreListDocument(originalContents, new IgnoreList("")), indexOfChange, from, to);
	}

	private int lineNumberForCharacterIndex(String originalContents, int indexOfChange) throws ConfigurationException {
		return newReportItem(originalContents, "from", "to", indexOfChange).lineNumber();
	}

	public void testXmlEncoding() throws ConfigurationException {
		String fromSomethingThatNeedsXmlEncoding = "< > & \"";
		String toSomethingThatNeedsXmlEncoding = fromSomethingThatNeedsXmlEncoding;
		String expectedXML = "    <ChangeThatDidNotCauseTestsToFail\r\n" + "      index=\"3\" line=\"1\"\r\n"
				+ "      from=\"&lt; &gt; &amp; &quot;\" to=\"&lt; &gt; &amp; &quot;\"\r\n" + "      file=\"sourceFileName\">\r\n"
				+ "sourceFileName - changed source on line 1 (char index=3) from &lt; > &amp; \" to &lt; > &amp; \"\r\n" + "ori>>>ginalContents\r\n"
				+ "    </ChangeThatDidNotCauseTestsToFail>";
		String actualXML = newReportItem("originalContents", fromSomethingThatNeedsXmlEncoding, toSomethingThatNeedsXmlEncoding, 3).asXML();
		assertEquals("should encode as XML as appropriate", Util.withoutWhitespace(expectedXML), Util.withoutWhitespace(actualXML));
	}

	public void testLineNumberOneLine() throws SourceChangeException {
		String oneLine = "some text";
		int indexOnOneLine = 3;
		assertEquals("on one line", 1, lineNumberForCharacterIndex(oneLine, indexOnOneLine));

		int nonExistant = -1;
		assertEquals("non-existant", -1, lineNumberForCharacterIndex(oneLine, nonExistant));

		int pastEnd = 9;
		assertEquals("past end", -1, lineNumberForCharacterIndex(oneLine, pastEnd));
	}

	public void testLineNumberSomeLines() throws SourceChangeException {
		String someLines = "once upon a time\nin a land far far away\nthere lived a giant";
		int onFirstLine = 3;
		assertEquals("on first line", 1, lineNumberForCharacterIndex(someLines, onFirstLine));

		int onSecondLine = 18;
		assertEquals("on second line", 2, lineNumberForCharacterIndex(someLines, onSecondLine));

		int onThirdLine = 42;
		assertEquals("on third line", 3, lineNumberForCharacterIndex(someLines, onThirdLine));
	}
}