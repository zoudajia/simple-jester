package jester.tests;

import jester.ConfigurationException;
import jester.IgnoreList;
import jester.IgnoreListDocument;
import junit.framework.TestCase;

public class IgnoreListDocumentTest extends TestCase {
	public IgnoreListDocumentTest(String arg0) {
		super(arg0);
	}

	public void testTextIsIgnored() throws ConfigurationException {
		IgnoreList ignoreList = new IgnoreList("%/*%*/");
		String source = "mary had a /*little*/ lamb";
		IgnoreListDocument document = new IgnoreListDocument(source, ignoreList);

		assertEquals(source.indexOf("lamb"), document.indexOf("l", 0));
	}

	public void testTextIsIgnoredForMultipleIgnores() throws ConfigurationException {
		IgnoreList ignoreList = new IgnoreList("%/*%*/\n%//start%//end");
		String source = "mary had a /*little*/ lamb, //start she eat it //end with mint sauce";
		IgnoreListDocument document = new IgnoreListDocument(source, ignoreList);

		assertEquals("didn't ignore /* */ region", source.indexOf("lamb"), document.indexOf("l", 0));
		assertEquals("didn't ignore //start //end region", source.indexOf("th mint"), document.indexOf("t", 0));
	}

	public void testCharAtReturnsSpaceInsideIgnoreRegion() throws ConfigurationException {
		IgnoreList ignoreList = new IgnoreList("%/*%*/");
		String source = "mary had a /*little*/ lamb";
		IgnoreListDocument document = new IgnoreListDocument(source, ignoreList);

		assertEquals(' ', document.charAt(source.indexOf('l')));
	}
}
