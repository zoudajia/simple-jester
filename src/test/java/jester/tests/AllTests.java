package jester.tests;

import junit.framework.*;

public class AllTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jester.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(IgnoreListTest.class);
		suite.addTest(TestTesterTest.suite());
		suite.addTest(ReportItemTest.suite());
		suite.addTest(TwoStringSwappingCodeManglerTest.suite());
		suite.addTest(ReportTest.suite());
		suite.addTest(MutationsListTest.suite());
		suite.addTest(MainArgumentsTest.suite());
		suite.addTest(ConfigurationTest.suite());
		suite.addTest(XMLReportWriterTest.suite());
		suite.addTest(SimpleIntCodeManglerTest.suite());
		suite.addTestSuite(IgnoreListDocumentTest.class);
		suite.addTest(ClassTestTesterTest.suite());
		//$JUnit-END$
		return suite;
	}
}