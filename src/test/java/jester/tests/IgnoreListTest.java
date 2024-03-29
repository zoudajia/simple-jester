package jester.tests;

import java.util.ArrayList;
import java.util.List;

import jester.ConfigurationException;
import jester.IgnoreList;
import jester.IgnorePair;
import junit.framework.TestCase;

public class IgnoreListTest extends TestCase {

	public IgnoreListTest(String arg0) {
		super(arg0);
	}

	public void testReadingEmptyIgnoreValues() throws ConfigurationException {
		List<IgnorePair> expectedIgnorePairs = new ArrayList<IgnorePair>();
		String ignoreFileContents = "";
		IgnoreList ignoreList = new IgnoreList(ignoreFileContents);
		assertEquals(expectedIgnorePairs, ignoreList.ignorePairs());
	}

	public void testReadingIgnoreValues() throws ConfigurationException {
		List<IgnorePair> expectedIgnorePairs = new ArrayList<IgnorePair>();
		expectedIgnorePairs.add(ignorePair("/*", "*/"));
		String ignoreFileContents = "%/*%*/";
		IgnoreList ignoreList = new IgnoreList(ignoreFileContents);
		assertEquals(expectedIgnorePairs, ignoreList.ignorePairs());
	}

	public void testReadingIgnoreValuesEndOfLineSpecialCase() throws ConfigurationException {
		List<IgnorePair> expectedIgnorePairs = new ArrayList<IgnorePair>();
		expectedIgnorePairs.add(ignorePair("//", "\n"));
		String ignoreFileContents = "%//%\\n";
		IgnoreList ignoreList = new IgnoreList(ignoreFileContents);
		assertEquals(expectedIgnorePairs, ignoreList.ignorePairs());
	}

	public void testReadingMultipleIgnoreValues() throws ConfigurationException {
		List<IgnorePair> expectedIgnorePairs = new ArrayList<IgnorePair>();
		expectedIgnorePairs.add(ignorePair("/*", "*/"));
		expectedIgnorePairs.add(ignorePair("//jester_ignore_start", "//jester_ignore_end"));
		String ignoreFileContents = "%/*%*/" + "\n" + "&//jester_ignore_start&//jester_ignore_end";
		IgnoreList ignoreList = new IgnoreList(ignoreFileContents);
		assertEquals(expectedIgnorePairs, ignoreList.ignorePairs());
	}

	public void testReadingMultipleIgnoreBlankLines() throws ConfigurationException {
		List<IgnorePair> expectedIgnorePairs = new ArrayList<IgnorePair>();
		expectedIgnorePairs.add(ignorePair("/*", "*/"));
		expectedIgnorePairs.add(ignorePair("//jester_ignore_start", "//jester_ignore_end"));
		String ignoreFileContents = "%/*%*/" + "\n\n\n" + "&//jester_ignore_start&//jester_ignore_end";
		IgnoreList ignoreList = new IgnoreList(ignoreFileContents);
		assertEquals(expectedIgnorePairs, ignoreList.ignorePairs());
	}

	public void testReadingIncorrectFile() {
		String ignoreFileContents = "%/*";
		try {
			IgnoreList ignoreList = new IgnoreList(ignoreFileContents);
			ignoreList.ignorePairs();
		} catch (ConfigurationException ex) {
			// pass
		}
	}

	private IgnorePair ignorePair(String start, String end) {
		return new IgnorePair(start, end);
	}
}
