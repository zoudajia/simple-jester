package jester.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import jester.Configuration;
import jester.RealConfiguration;
import junit.framework.TestCase;

public class ConfigurationTest extends TestCase {
	private static PrintStream NullErrorStream = new PrintStream(new ByteArrayOutputStream());

	public ConfigurationTest(String name) {
		super(name);
	}

	public void testDefaults() throws IOException {
		Configuration config = new RealConfiguration("there must be no file called this", NullErrorStream); // there
																											// is
																											// no
																											// file
																											// called
																											// "there
																											// must
																											// be
																											// no
																											// file
																											// called
																											// this"

		assertTrue(!config.shouldReportEagerly());
		assertEquals(".java", config.sourceFileExtension());
		assertEquals("BUILD SUCCESSFUL", config.buildPassString());

		assertTrue(config.closeUIOnFinish());
	}
}