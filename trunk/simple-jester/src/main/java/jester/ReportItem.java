package jester;

import java.util.StringTokenizer;

public class ReportItem {

	private String sourceFileName;
	private IgnoreListDocument originalContents;
	private int indexOfChange;
	private String valueChangedFrom;
	private String valueChangedTo;

	public ReportItem(String sourceFileName, IgnoreListDocument originalContents, int indexOfChange, String valueChangedFrom, String valueChangedTo) {

		super();
		this.sourceFileName = sourceFileName;
		this.originalContents = originalContents;
		this.indexOfChange = indexOfChange;
		this.valueChangedFrom = valueChangedFrom;
		this.valueChangedTo = valueChangedTo;
	}

	public String toString() {

		StringBuffer result = new StringBuffer();
		result.append(sourceFileName + " - changed source on line " + lineNumber() + " (char index=" + indexOfChange + ") from " + valueChangedFrom + " to " + valueChangedTo
				+ "\n");
		int start = Math.max(0, indexOfChange - 30);
		int end = Math.min(originalContents.length(), indexOfChange + 45);
		result.append(originalContents.substring(start, indexOfChange));
		result.append(">>>");
		result.append(originalContents.substring(indexOfChange, end));
		return result.toString();

	}

	/*
	 * @return a negative integer, zero, or a positive integer as the first
	 * argument is less than, equal to, or greater than the second.
	 */
	public int compareToReportItem(ReportItem other) {
		int nameCompare = sourceFileName.compareTo(other.sourceFileName);
		if (nameCompare == 0) {
			return indexOfChange - other.indexOfChange;
		}
		return nameCompare;
	}

	public int lineNumber() {

		if (indexOfChange == -1) {
			return -1;
		}
		if (indexOfChange >= originalContents.length()) {
			return -1;
		}
		StringTokenizer aLineBreaker = new StringTokenizer(originalContents.substring(0, indexOfChange), "\n", false);
		int result = 0;
		while (aLineBreaker.hasMoreTokens()) {
			result++;
			aLineBreaker.nextToken();
		}
		return result;

	}

	public String asXML() {

		StringBuffer xml = new StringBuffer();
		xml.append("    <ChangeThatDidNotCauseTestsToFail\n      index=\"" + indexOfChange + "\" line=\"" + lineNumber() + "\"" + "\n      from=\"" + xmlEncoded(valueChangedFrom)
				+ "\" to=\"" + xmlEncoded(valueChangedTo) + "\"\n      file=\"" + sourceFileName + "\">\n");
		xml.append(pcdataEncoded(this.toString()));
		xml.append("\n    </ChangeThatDidNotCauseTestsToFail>");
		return xml.toString();

	}

	private String xmlEncoded(String aString) {

		int length = aString.length();
		StringBuffer result = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			char nextChar = aString.charAt(i);
			switch (nextChar) {
			case '&':
				result.append("&amp;");
				break;
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '"':
				result.append("&quot;");
				break;
			default:
				result.append(nextChar);
			}
		}
		return result.toString();

	}

	private String pcdataEncoded(String aString) {

		int length = aString.length();
		StringBuffer result = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			char nextChar = aString.charAt(i);
			switch (nextChar) {
			case '&':
				result.append("&amp;");
				break;
			case '<':
				result.append("&lt;");
				break;
			default:
				result.append(nextChar);
			}
		}
		return result.toString();

	}

}