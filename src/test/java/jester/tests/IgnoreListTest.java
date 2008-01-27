package jester.tests;

import jester.*;

import java.util.*;
import junit.framework.TestCase;

public class IgnoreListTest extends TestCase {

	public IgnoreListTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(IgnoreListTest.class);
	}

	public void testReadingEmptyIgnoreValues() throws ConfigurationException {
		List expectedIgnorePairs = new ArrayList();
		String ignoreFileContents = "";
		IgnoreList ignoreList = new IgnoreList(ignoreFileContents);
		assertEquals(expectedIgnorePairs, ignoreList.ignorePairs());
	}

	public void testReadingIgnoreValues() throws ConfigurationException {
		List expectedIgnorePairs = new ArrayList();
		expectedIgnorePairs.add(ignorePair("/*", "*/"));
		String ignoreFileContents = "%/*%*/";
		IgnoreList ignoreList = new IgnoreList(ignoreFileContents);
		assertEquals(expectedIgnorePairs, ignoreList.ignorePairs());
	}

	public void testReadingIgnoreValuesEndOfLineSpecialCase() throws ConfigurationException {
		List expectedIgnorePairs = new ArrayList();
		expectedIgnorePairs.add(ignorePair("//", "\n"));
		String ignoreFileContents = "%//%\\n";
		IgnoreList ignoreList = new IgnoreList(ignoreFileContents);
		assertEquals(expectedIgnorePairs, ignoreList.ignorePairs());
	}

	public void testReadingMultipleIgnoreValues() throws ConfigurationException {
		List expectedIgnorePairs = new ArrayList();
		expectedIgnorePairs.add(ignorePair("/*", "*/"));
		expectedIgnorePairs.add(ignorePair("//jester_ignore_start", "//jester_ignore_end"));
		String ignoreFileContents = "%/*%*/" + "\n" + "&//jester_ignore_start&//jester_ignore_end";
		IgnoreList ignoreList = new IgnoreList(ignoreFileContents);
		assertEquals(expectedIgnorePairs, ignoreList.ignorePairs());
	}

	public void testReadingMultipleIgnoreBlankLines() throws ConfigurationException {
		List expectedIgnorePairs = new ArrayList();
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
			//pass
		}
	}

	private IgnorePair ignorePair(String start, String end) {
		return new IgnorePair(start, end);
	}
}
