package jester;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class RealReport implements Report {
	private Configuration myConfiguration;
	private PrintWriter myOutput;
	private int myTotalNumberOfChangesThatCausedTestsToFail = 0;
	private int myTotalNumberOfChangesThatDidNotCauseTestsToFail = 0;

	private List<ReportItem> myFileChangesThatDidNotCauseTestsToFail = new Vector<ReportItem>();
	private int myNumberOfFileChangesThatCausedTestsToFail = 0;
	private IgnoreListDocument myOriginalContents;
	private String mySourceFileName;
	private XMLReportWriter myXMLReportWriter;
	private ProgressReporter myProgressReporter;

	public RealReport(Configuration configuration, PrintWriter aPrintWriter, XMLReportWriter anXMLReportWriter, ProgressReporter aProgressReporter) {
		myConfiguration = configuration;
		myOutput = aPrintWriter;
		myXMLReportWriter = anXMLReportWriter;
		myProgressReporter = aProgressReporter;
	}

	public void setNumberOfFilesThatWillBeTested(int numberOfFilesThatWillBeTested) {
		myProgressReporter.setMaximum(numberOfFilesThatWillBeTested);
	}

	private void reportFileProgress() {
		myProgressReporter.progress();
	}

	private void redBar() {
		myProgressReporter.setColor(Color.red);
	}

	private void greenBar() {
		myProgressReporter.setColor(Color.green);
	}

	public void startFile(String sourceFileName, IgnoreListDocument originalContents) throws SourceChangeException {
		if (mySourceFileName != null) {
			throw new SourceChangeException("Cannot start a file until finished previous one (tried to start " + sourceFileName + " when already doing " + mySourceFileName + ")");
		}
		myNumberOfFileChangesThatCausedTestsToFail = 0;
		myFileChangesThatDidNotCauseTestsToFail = new Vector<ReportItem>();
		mySourceFileName = sourceFileName;
		myOriginalContents = originalContents;
	}

	public void finishFile(String sourceFileName) throws SourceChangeException {
		if (!mySourceFileName.equals(sourceFileName)) {
			throw new SourceChangeException("Cannot finish a different file to the one you started (finished " + sourceFileName + " but expected " + mySourceFileName + ")");
		}
		reportFileProgress();
		printFileChanges();
		writeChangesToXMLFile();
		mySourceFileName = null;
		myOriginalContents = null;
	}

	public void changeThatCausedTestsToFail(int indexOfChange, String valueChangedFrom, String valueChangedTo) throws SourceChangeException {
		if (mySourceFileName == null) {
			throw new SourceChangeException("Cannot report change of file haven't started (internal error1)");
		}
		greenBar();
		ReportItem aReportItem = new ReportItem(mySourceFileName, myOriginalContents, indexOfChange, valueChangedFrom, valueChangedTo);
		myProgressReporter.setText(aReportItem.toString());
		myNumberOfFileChangesThatCausedTestsToFail++;
		myTotalNumberOfChangesThatCausedTestsToFail++;
	}

	public void changeThatDidNotCauseTestsToFail(int indexOfChange, String valueChangedFrom, String valueChangedTo) throws SourceChangeException {
		if (mySourceFileName == null) {
			throw new SourceChangeException("Cannot report change of file haven't started (internal error2)");
		}
		redBar();
		myTotalNumberOfChangesThatDidNotCauseTestsToFail++;
		ReportItem aReportItem = new ReportItem(mySourceFileName, myOriginalContents, indexOfChange, valueChangedFrom, valueChangedTo);
		myProgressReporter.setText(aReportItem.toString());

		if (myConfiguration.shouldReportEagerly()) {
			myOutput.println(aReportItem.toString());
			myOutput.flush();
		}

		myFileChangesThatDidNotCauseTestsToFail.add(aReportItem);
	}

	public String toString() {
		String summary = myTotalNumberOfChangesThatDidNotCauseTestsToFail + " mutations survived out of " + totalNumberOfChanges() + " changes. Score = " + totalScore();
		return summary;
	}

	private int fileNumberOfChanges() {
		return myNumberOfFileChangesThatCausedTestsToFail + fileNumberOfChangesThatDidNotCauseTestsToFail();
	}

	public int fileScore() {
		return score(fileNumberOfChanges(), fileNumberOfChangesThatDidNotCauseTestsToFail());
	}

	private void printFileChanges() {
		String summary = "For File " + mySourceFileName + ": " + fileNumberOfChangesThatDidNotCauseTestsToFail() + " mutations survived out of " + fileNumberOfChanges()
				+ " changes. Score = " + fileScore();

		Object[] sortedReportItems = sortedReportItems();
		StringBuffer result = new StringBuffer();
		result.append(summary + "\n");
		for (int i = 0; i < sortedReportItems.length; i++) {
			result.append(sortedReportItems[i] + "\n");
		}
		myOutput.println(result.toString());
		myOutput.flush();
	}

	/*
	 * @return a negative integer, zero, or a positive integer as the first
	 * argument is less than, equal to, or greater than the second.
	 */
	private Comparator<ReportItem> reportItemComparitor() {
		return new Comparator<ReportItem>() {
			public int compare(ReportItem o1, ReportItem o2) {
				return o1.compareToReportItem(o2);
			}
		};
	}

	private int score(int numberOfChanges, int numberOfChangesThatDidNotCauseTestsToFail) {
		int score = INITIAL_SCORE;
		if (numberOfChanges != 0) {
			score = 100 - ((numberOfChangesThatDidNotCauseTestsToFail * 100) / numberOfChanges);
		}
		return score;
	}

	private int totalNumberOfChanges() {
		return myTotalNumberOfChangesThatCausedTestsToFail + myTotalNumberOfChangesThatDidNotCauseTestsToFail;
	}

	public int totalScore() {
		return score(totalNumberOfChanges(), myTotalNumberOfChangesThatDidNotCauseTestsToFail);
	}

	private int fileNumberOfChangesThatDidNotCauseTestsToFail() {
		return myFileChangesThatDidNotCauseTestsToFail.size();
	}

	private ReportItem[] sortedReportItems() {
		ReportItem[] result = myFileChangesThatDidNotCauseTestsToFail.toArray(new ReportItem[0]);
		Arrays.sort(result, reportItemComparitor());
		return result;
	}

	private void writeChangesToXMLFile() throws SourceChangeException {
		myXMLReportWriter.writeXMLReport(sortedReportItems(), mySourceFileName, fileNumberOfChangesThatDidNotCauseTestsToFail(), fileNumberOfChanges(), fileScore());
	}
}