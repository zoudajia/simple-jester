package jester.tests;

import jester.CodeMangler;
import jester.IgnoreList;
import jester.SimpleIntCodeMangler;
import jester.SourceChangeException;
import junit.framework.TestCase;

public class SimpleIntCodeManglerTest extends TestCase {
	public SimpleIntCodeManglerTest(String name) {
		super(name);
	}

	public void testContinuesIfChangeFails() throws SourceChangeException {
		MockClassSourceChanger mockClassSourceChanger = new MockClassSourceChanger();
		mockClassSourceChanger.setThrowExceptionOnFirstChangeAttempt(true);
		mockClassSourceChanger.setOriginalContents("abc 3 jshf 8 def");

		CodeMangler codeMangler = new SimpleIntCodeMangler(mockClassSourceChanger);

		mockClassSourceChanger.setExpectedWriteOverSourceReplacingCalls(2); // first
																			// one
																			// values
																			// are
																			// ignored
		mockClassSourceChanger.setExpectedWriteOverSourceReplacing("abc 3 jshf ".length(), "8", "9");
		assertTrue("could make change of 8 to 9 (can't change 3 to 4)", codeMangler.makeChangeToClass());
		mockClassSourceChanger.setExpectedWriteOriginalBackCalls(1);
		assertTrue("could not make a subsequent change", !codeMangler.makeChangeToClass());

		mockClassSourceChanger.verify();
	}

	public void testContinuesIfChangeSucceeds() throws SourceChangeException {
		MockClassSourceChanger mockClassSourceChanger = new MockClassSourceChanger();
		mockClassSourceChanger.setThrowExceptionOnFirstChangeAttempt(false);
		mockClassSourceChanger.setOriginalContents("abc 3 jshf 8 def");

		CodeMangler codeMangler = new SimpleIntCodeMangler(mockClassSourceChanger);

		mockClassSourceChanger.setExpectedWriteOverSourceReplacingCalls(2);
		mockClassSourceChanger.setExpectedWriteOverSourceReplacing("abd ".length(), "3", "4");
		assertTrue("could make change of 3 to 4", codeMangler.makeChangeToClass());
		mockClassSourceChanger.setExpectedWriteOverSourceReplacing("abc 3 jshf ".length(), "8", "9");
		assertTrue("could make another change", codeMangler.makeChangeToClass());
		mockClassSourceChanger.setExpectedWriteOriginalBackCalls(1);
		assertTrue("could not make another change", !codeMangler.makeChangeToClass());

		mockClassSourceChanger.verify();
	}

	public void testCouldntMangle() throws SourceChangeException {
		MockClassSourceChanger mockClassSourceChanger = new MockClassSourceChanger();
		mockClassSourceChanger.setOriginalContents("abc def");
		mockClassSourceChanger.setExpectedWriteOverSourceReplacingCalls(0);

		CodeMangler codeMangler = new SimpleIntCodeMangler(mockClassSourceChanger);

		assertTrue("couldn't make change - mock checks didnt try to make any change", !codeMangler.makeChangeToClass());

		mockClassSourceChanger.verify();
	}

	public void testDoesntTryToChangeBadlyNamedMethodCalls() throws SourceChangeException {
		MockClassSourceChanger mockClassSourceChanger = new MockClassSourceChanger();
		mockClassSourceChanger.setOriginalContents("abc method3 def");
		mockClassSourceChanger.setExpectedWriteOverSourceReplacingCalls(0);

		CodeMangler codeMangler = new SimpleIntCodeMangler(mockClassSourceChanger);

		assertTrue("couldn't make change - mock checks didnt try to make any change", !codeMangler.makeChangeToClass());

		mockClassSourceChanger.verify();
	}

	public void testSimpleMangle() throws SourceChangeException {
		MockClassSourceChanger mockClassSourceChanger = new MockClassSourceChanger();
		mockClassSourceChanger.setOriginalContents("abc 1 def");
		mockClassSourceChanger.setExpectedWriteOverSourceReplacing(4, "1", "2");

		CodeMangler codeMangler = new SimpleIntCodeMangler(mockClassSourceChanger);

		assertTrue("could make change - mock checks make expected change", codeMangler.makeChangeToClass());
	}

	public void testBug955882() throws SourceChangeException {
		MockClassSourceChanger mockClassSourceChanger = new MockClassSourceChanger();
		mockClassSourceChanger.setOriginalContents("int Log2(float n) { return Log(2,n); }");
		mockClassSourceChanger.setExpectedWriteOverSourceReplacing(31, "2", "3");

		CodeMangler codeMangler = new SimpleIntCodeMangler(mockClassSourceChanger);

		assertTrue("could make change - mock checks make expected change", codeMangler.makeChangeToClass());
	}

	public void testBugIgnoreRegionsNotBeingIgnored() throws SourceChangeException {
		MockClassSourceChanger mockClassSourceChanger = new MockClassSourceChanger();
		mockClassSourceChanger.setOriginalContents("foo //1\n 1 bar", new IgnoreList("x//x\\n"));
		mockClassSourceChanger.setExpectedWriteOverSourceReplacing(9, "1", "2");

		CodeMangler codeMangler = new SimpleIntCodeMangler(mockClassSourceChanger);

		assertTrue("could make change - mock checks make expected change", codeMangler.makeChangeToClass());
	}

	public void testSimpleMangleRollover() throws SourceChangeException {
		MockClassSourceChanger mockClassSourceChanger = new MockClassSourceChanger();
		mockClassSourceChanger.setOriginalContents("abc 9 def");
		mockClassSourceChanger.setExpectedWriteOverSourceReplacing(4, "9", "0");

		CodeMangler codeMangler = new SimpleIntCodeMangler(mockClassSourceChanger);

		assertTrue("could make change - mock checks make expected change", codeMangler.makeChangeToClass());
	}
}