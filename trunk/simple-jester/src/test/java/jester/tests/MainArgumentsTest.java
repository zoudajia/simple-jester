package jester.tests;

import jester.FileExistenceChecker;
import jester.JesterArgumentException;
import jester.MainArguments;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MainArgumentsTest extends TestCase {	
	FileExistenceChecker anythingFileExistenceChecker = new FileExistenceChecker(){
		public boolean exists(String fileName) {
			return true;
		}
	};
	FileExistenceChecker nothingFileExistenceChecker = new FileExistenceChecker(){
		public boolean exists(String fileName) {
			return false;
		}
	};
	
	public MainArgumentsTest(String name) {
		super(name);
	}
	public static void main(String args[]) {
		junit.awtui.TestRunner.main(new String[] { MainArgumentsTest.class.getName() });
	}
	public static Test suite() {
		TestSuite suite = new TestSuite(MainArgumentsTest.class);
		return suite;
	}

	public void testMissingArgumentsCauseExceptionToBeThrown() throws Exception {
		try {
			new MainArguments(new String[]{}, anythingFileExistenceChecker);
			fail();
		}
		catch (JesterArgumentException e) {
			// exception was expected, everything is ok
		}
		
		try {
			new MainArguments(new String[]{"a"}, anythingFileExistenceChecker);
			fail();
		}
		catch (JesterArgumentException e) {
			// exception was expected, everything is ok
		}
	}	
	
	public void testMissingFileCauseExceptionToBeThrown() throws Exception {
		try {
			new MainArguments(new String[]{"a", "b"}, nothingFileExistenceChecker);
			fail("Expected exception to be thrown because file doesn't exist");
		}
		catch (JesterArgumentException e) {
			// exception was expected, everything is ok
		}
	}	
	
	public void testMandatoryArguments() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[]{"a", "b"}, anythingFileExistenceChecker);
		assertEquals("a", args.getBuildRunningCommand());
		assertEquals(new String[]{"b"}, args.getDirectoryOrFileNames());
	}
	
	public void testThatMultipleMutationDirectoriesOrFilesAreAllowed() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[]{"a", "b1", "b2", "b3"}, anythingFileExistenceChecker);
		assertEquals("a", args.getBuildRunningCommand());
		assertEquals(new String[]{"b1", "b2", "b3"}, args.getDirectoryOrFileNames());
	}
	
	public void testThatOneCanSpecifyOptionToNotShowProgressDialog() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[]{"a", "b"}, anythingFileExistenceChecker);
		assertTrue(args.shouldShowProgressDialog());
		
		args = new MainArguments(new String[]{"a", "-q", "b"}, anythingFileExistenceChecker);
		assertFalse(args.shouldShowProgressDialog());
	}
	
	private void assertEquals(String[] expected, String[] actual){
		assertEquals(expected.length,actual.length);
		for (int i = 0; i < actual.length; i++) {
			assertEquals(expected[i],actual[i]);
		}
	}
}