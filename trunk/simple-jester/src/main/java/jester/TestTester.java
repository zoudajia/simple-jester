package jester;

import java.io.*;

/**
 * Copyright (2000-2008) Ivan Moore
 *
 * see http://jester.sourceforge.net
 * for updates, FAQs etc.
 *
 * JUnit Software and Software Derivatives are available only
 * under the terms of the JUnit licence.
 *
 * This software is only available under the terms of its licence:
 *
 * Copyright (c) 2000-2008 Ivan Moore

	All rights reserved.

	Permission is hereby granted, free of charge, to any person obtaining a
	copy of this software and associated documentation files (the
	"Software"), to deal in the Software without restriction, including
	without limitation the rights to use, copy, modify, merge, publish,
	distribute, and/or sell copies of the Software, and to permit persons
	to whom the Software is furnished to do so, provided that the above
	copyright notice(s) and this permission notice appear in all copies of
	the Software and that both the above copyright notice(s) and this
	permission notice appear in supporting documentation.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
	OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT
	OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
	HOLDERS INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL
	INDIRECT OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING
	FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
	NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
	WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

	Except as contained in this notice, the name of a copyright holder
	shall not be used in advertising or otherwise to promote the sale, use
	or other dealings in this Software without prior written authorization
	of the copyright holder.
 */

public class TestTester {
	public static final String VERSION = "1.2";

	public static final String TIMEOUT_FILENAME = "jesterTimeout.txt";

	private TestRunner testRunner;
	private ClassIterator classIterator;
	private ClassTestTester classTestTester;
    
	private static MainArguments mainArguments;
    
	public TestTester(TestRunner testRunner, ClassIterator classIterator, ClassTestTester classTestTester) {
		this.testRunner = testRunner;
		this.classIterator = classIterator;
		this.classTestTester = classTestTester;
	}

	public static void main(String args[]) {
		try {
			try {
				mainArguments = new MainArguments(args, new FileExistenceChecker(){
					public boolean exists(String fileName) {
						return new File(fileName).exists();
					}
				});
			}
			catch (JesterArgumentException e) {
				System.out.println(e);
				System.out.println();
				MainArguments.printUsage(System.out, VERSION);
				System.exit(0);
			}
			
			deleteTimeoutFile();
	
			long runTime = doMain();
			System.out.println("took " + (runTime / (1000 * 60)) + " minutes");
		}
		catch (SourceChangeException e) {
            System.err.println("Jesting failed!");
            e.printStackTrace();
			// what to print????
		}
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            // We may need to close the window now.
            // This is ugly. Could we just not bring it up?
            // The issue arises when the tests failup front.
            System.exit(1);            
        }
	}
    
	private static long doMain() throws IOException, SourceChangeException {
        long t1 = System.currentTimeMillis();
        Configuration configuration = new RealConfiguration(RealConfiguration.DEFAULT_CONFIGURATION_FILENAME);
	    Report aReport = null;
        OutputStream out = new FileOutputStream(configuration.xmlReportFileName());
		Writer reportFileWriter = new OutputStreamWriter(out, "UTF-8");
		XMLReportWriter anXMLReportWriter = new RealXMLReportWriter(reportFileWriter);
		try {
            reportFileWriter.write("<?xml version='1.0'?>\n");// TODO can be tidied up - XMLReportWriter should do this
            reportFileWriter.write("<JesterReport>\n");// TODO can be tidied up - XMLReportWriter should do this

			ProgressReporter progressReporter = mainArguments.shouldShowProgressDialog() ? 
												new RealProgressReporter(configuration) : 
                            //the cast should not be necessary but Eclipse's compiler thinks it is
							(ProgressReporter)	new NullProgressReporter();
                                               
			aReport = new RealReport(configuration, new PrintWriter(System.out), anXMLReportWriter, progressReporter);

			String[] directoryNames = mainArguments.getDirectoryOrFileNames();
			ClassIterator classIterator = new FileBasedClassIterator(configuration, directoryNames, aReport);

			String buildRunningCommand = mainArguments.getBuildRunningCommand();
			TestRunner testRunner = new RealTestRunner(configuration, buildRunningCommand);

			ClassTestTester classTestTester = new RealClassTestTester(testRunner, new RealMutationsList(RealMutationsList.DEFAULT_MUTATIONS_FILENAME));

			TestTester testTester = new TestTester(testRunner, classIterator, classTestTester);
			testTester.run();
		}
        finally {
			reportFileWriter.write("</JesterReport>");
			reportFileWriter.close();
		}
		long t2 = System.currentTimeMillis();
		if (aReport != null) {
			System.out.println(aReport);
		}
		long runTime = t2-t1;
		return runTime;
	}
    
	public void run() throws SourceChangeException {
        // XXX need to print more detailed info about why tests failed;
        // not just pass-fail
		boolean testsPassedBeforeAnyChanges = testRunner.testsRunWithoutFailures();
		if (!testsPassedBeforeAnyChanges) {
			throw new SourceChangeException(
             "Couldn't run test tester because tests didn't pass before any changes made - see FAQ at http://jester.sourceforge.net");
		}
		classIterator.iterate(classTestTester);
	}

	private static void deleteTimeoutFile() {
		File timeoutFile = new File(TIMEOUT_FILENAME);
		if (timeoutFile.exists()) {
			timeoutFile.delete();
		}
	}
}