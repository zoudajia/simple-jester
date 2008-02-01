package jester.tests;

import jester.*;
import junit.framework.*;

public class TwoStringSwappingCodeManglerTest extends TestCase {
	private String originalString1 = "one", replacementString1 = "uno";

	public TwoStringSwappingCodeManglerTest(String name) {
		super(name);
	}

	private CodeMangler newCodeMangler(ClassSourceCodeChanger sourceCodeSystem) {
		return new TwoStringSwappingCodeMangler(sourceCodeSystem, originalString1, replacementString1);
	}

	public void testCouldntMangle() throws SourceChangeException {
		MockClassSourceChanger mockClassSourceChanger = new MockClassSourceChanger();
		mockClassSourceChanger.setOriginalContents("abc def");
		mockClassSourceChanger.setExpectedWriteOverSourceReplacingCalls(0);

		CodeMangler codeMangler = newCodeMangler(mockClassSourceChanger);

		assertTrue("couldn't make change - mock checks didnt try to make any change", !codeMangler.makeChangeToClass());

		mockClassSourceChanger.verify();
	}

	public void testSimpleMangle1() throws SourceChangeException {
		MockClassSourceChanger mockClassSourceChanger = new MockClassSourceChanger();
		mockClassSourceChanger.setOriginalContents("abc " + originalString1 + " def");
		mockClassSourceChanger.setExpectedWriteOverSourceReplacing(4, originalString1, replacementString1);

		CodeMangler codeMangler = newCodeMangler(mockClassSourceChanger);

		assertTrue("could make change - mock checks make expected change", codeMangler.makeChangeToClass());
	}
}