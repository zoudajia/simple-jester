package jester.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jester.FileExistenceChecker;
import jester.JesterArgumentException;
import jester.MainArguments;

public class MainArgumentsTest {
	private final FileExistenceChecker anythingFileExistenceChecker = new FileExistenceChecker() {
		public boolean exists(String fileName) {
			return true;
		}
	};
	
	private final FileExistenceStub nothingFileExistenceChecker = new FileExistenceStub();

	@Test
	public void mandatoryArgumentsAreParsedOut() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[] { "-buildCommand", "ant", "-source", "src" }, anythingFileExistenceChecker);
		assertEquals("ant", args.getBuildRunningCommand());
		assertEquals(Arrays.asList(new String[] { "src" }), args.getDirectoryOrFileNames());
	}
	
	@Test
	public void canHaveManySourceDirectoriesOrFiles() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[] { "-buildCommand", "ant", "-source", "src1", "src2", "src3", "src4" }, anythingFileExistenceChecker);
		assertEquals("ant", args.getBuildRunningCommand());
		assertEquals(Arrays.asList(new String[] { "src1", "src2", "src3", "src4" }), args.getDirectoryOrFileNames());
	}
	
	@Test
	public void orderOfArgsDoesNotMatter() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[] { "-source", "src1", "src2", "src3", "src4", "-buildCommand", "ant" }, anythingFileExistenceChecker);
		assertEquals("ant", args.getBuildRunningCommand());
		assertEquals(Arrays.asList(new String[] { "src1", "src2", "src3", "src4" }), args.getDirectoryOrFileNames());
	}
	
	@Test
	public void ignoreListCanBeSpecified() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-ignore", "foo.txt" }, anythingFileExistenceChecker);
		assertEquals("foo.txt", args.getIgnoreListFileName());
	}
	
	@Test(expected = JesterArgumentException.class)
	public void onlyOneIgnoreListCanBeSpecified() throws JesterArgumentException {
		new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-ignore", "foo.txt", "bar.txt" }, anythingFileExistenceChecker);
	}
	
	@Test(expected = JesterArgumentException.class)
	public void ignoreListMustExist() throws JesterArgumentException {
		new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-ignore", "foo.txt" }, new FileExistenceStub("src"));
	}
	
	@Test
	public void shouldShowProgressDialogCanBeSpecified() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-shouldShowProgressDialog", "true" }, anythingFileExistenceChecker);
		assertTrue(args.shouldShowProgressDialog());
	}
	
	@Test(expected = JesterArgumentException.class)
	public void shouldShowProgressDialogCanBeOnlyBeSpecifiedOnce() throws JesterArgumentException {
		new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-shouldShowProgressDialog", "true", "false" }, anythingFileExistenceChecker);
	}
	
	@Test
	public void anythingOtherThanTrueIsFalseForSpecifyingShouldShowProgressDialog() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-shouldShowProgressDialog", "maybe" }, anythingFileExistenceChecker);
		assertFalse(args.shouldShowProgressDialog());
	}
	
	@Test
	public void shouldShowProgressDialogDefaultsToTrue() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant" }, anythingFileExistenceChecker);
		assertTrue(args.shouldShowProgressDialog());
	}
	
	@Test
	public void mutationListCanBeSpecified() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-mutations", "foo.txt" }, anythingFileExistenceChecker);
		assertEquals("foo.txt", args.getMutationsFileName());
	}
	
	@Test(expected = JesterArgumentException.class)
	public void onlyOneMutationListCanBeSpecified() throws JesterArgumentException {
		new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-mutations", "foo.txt", "bar.txt" }, anythingFileExistenceChecker);
	}
	
	@Test(expected = JesterArgumentException.class)
	public void mutationListMustExist() throws JesterArgumentException {
		new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-mutations", "foo.txt" }, new FileExistenceStub("src"));
	}
	
	@Test
	public void configCanBeSpecified() throws JesterArgumentException {
		MainArguments args = new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-config", "foo.txt" }, anythingFileExistenceChecker);
		assertEquals("foo.txt", args.getConfigFileName());
	}
	
	@Test
	public void nonManditoryArgumentsAreNullIfNotSpecified() throws Exception {
		MainArguments args = new MainArguments(new String[] { "-buildCommand", "ant", "-source", "src" }, anythingFileExistenceChecker);
		
		assertNull(args.getConfigFileName());
		assertNull(args.getIgnoreListFileName());
		assertNull(args.getMutationsFileName());
	}
	
	@Test(expected = JesterArgumentException.class)
	public void onlyOneConfigCanBeSpecified() throws JesterArgumentException {
		new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-config", "foo.txt", "bar.txt" }, anythingFileExistenceChecker);
	}
	
	@Test(expected = JesterArgumentException.class)
	public void configMustExist() throws JesterArgumentException {
		new MainArguments(new String[] { "-source", "src", "-buildCommand", "ant", "-config", "foo.txt" }, new FileExistenceStub("src"));
	}

	@Test(expected = JesterArgumentException.class)
	public void missingArgNameCausesExceptionToBeThrown() throws Exception {
		new MainArguments(new String[] {"whatever", "-buildCommand", "ant", "-source", "src" }, anythingFileExistenceChecker);
	}

	@Test(expected = JesterArgumentException.class)
	public void missingBuildArgumentCausesExceptionToBeThrown() throws Exception {
		new MainArguments(new String[] {"-source", "src"}, anythingFileExistenceChecker);
	}
	
	@Test(expected = JesterArgumentException.class)
	public void missingSourceArgumentCausesExceptionToBeThrown() throws Exception {
		new MainArguments(new String[] {"-buildCommand", "ant"}, anythingFileExistenceChecker);
	}
	
	@Test(expected = JesterArgumentException.class)
	public void missingAllArgumentsCausesExceptionToBeThrown() throws Exception {
		new MainArguments(new String[] {}, anythingFileExistenceChecker);
	}
	
	@Test(expected = JesterArgumentException.class)
	public void missingFileCausesExceptionToBeThrown() throws Exception {
		new MainArguments(new String[] {"-buildCommand", "ant", "-source", "src1"}, nothingFileExistenceChecker);
	}
	
	@Test(expected = JesterArgumentException.class)
	public void moreThanOneBuildCommandCausesExceptionToBeThrown() throws Exception {
		new MainArguments(new String[] {"-buildCommand", "ant", "fly", "-source", "src1"}, anythingFileExistenceChecker);
	}
	
	private static class FileExistenceStub implements FileExistenceChecker {
		private List<String> filesThatExist;
		
		public FileExistenceStub(String... filesThatExist) {
			this.filesThatExist = Arrays.asList(filesThatExist);
		}

		public boolean exists(String fileName) {
			return filesThatExist.contains(fileName);
		}
	}
}