package jester.tests;

import jester.ClassSourceCodeChanger;
import jester.CodeMangler;
import jester.IgnoreList;
import jester.IgnoreListDocument;
import jester.SourceChangeException;
import jester.TwoStringSwappingCodeMangler;
import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class TwoStringSwappingCodeManglerTest extends TestCase {
	private String originalString1 = "one", replacementString1 = "uno";
	private Mockery context = new Mockery();

	public TwoStringSwappingCodeManglerTest(String name) {
		super(name);
	}

	private CodeMangler newCodeMangler(ClassSourceCodeChanger sourceCodeSystem) {
		return new TwoStringSwappingCodeMangler(sourceCodeSystem, originalString1, replacementString1);
	}

	public void testCouldntMangle() throws SourceChangeException {
		final ClassSourceCodeChanger mockClassSourceChanger = context.mock(ClassSourceCodeChanger.class);
		context.checking(new Expectations(){{
			one(mockClassSourceChanger).getOriginalContents(); will(returnValue(new IgnoreListDocument("abc def", new IgnoreList(""))));
			//is this how to do "never"?
			never(mockClassSourceChanger).writeOverSourceReplacing(with(any(int.class)), with(any(String.class)), with(any(String.class)));
		}});

		CodeMangler codeMangler = newCodeMangler(mockClassSourceChanger);

		assertTrue("couldn't make change - mock checks didnt try to make any change", !codeMangler.makeChangeToClass());

		context.assertIsSatisfied();
	}

	public void testSimpleMangle1() throws SourceChangeException {
		final ClassSourceCodeChanger mockClassSourceChanger = context.mock(ClassSourceCodeChanger.class);
		context.checking(new Expectations(){{
			one(mockClassSourceChanger).getOriginalContents(); will(returnValue(new IgnoreListDocument("abc " + originalString1 + " def", new IgnoreList(""))));
			one(mockClassSourceChanger).writeOverSourceReplacing(4, originalString1, replacementString1);
		}});

		CodeMangler codeMangler = newCodeMangler(mockClassSourceChanger);

		assertTrue("could make change - mock checks make expected change", codeMangler.makeChangeToClass());
	}
}