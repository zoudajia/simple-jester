package jester.tests;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import jester.MutationMaker;
import jester.MutationsList;
import jester.RealMutationsList;
import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class MutationsListTest extends TestCase {
	private static PrintStream NullErrorStream = new PrintStream(new ByteArrayOutputStream());
	private Mockery context = new Mockery();
	
	public MutationsListTest(String name) {
		super(name);
	}

	public void testDefaults() throws Exception {
		final MutationMaker mockMutationMaker = context.mock(MutationMaker.class);
		context.checking(new Expectations(){{
			one(mockMutationMaker).mutate("true", "false");
			one(mockMutationMaker).mutate("false", "true");
			one(mockMutationMaker).mutate("if(", "if(true ||");
			one(mockMutationMaker).mutate("if (", "if (true ||");
			one(mockMutationMaker).mutate("if(", "if(false &&");
			one(mockMutationMaker).mutate("if (", "if (false &&");
			one(mockMutationMaker).mutate("==", "!=");
			one(mockMutationMaker).mutate("!=", "==");
		}});

		MutationsList aMutationsList = new RealMutationsList(null, NullErrorStream);
		aMutationsList.visit(mockMutationMaker);

		context.assertIsSatisfied();
	}

	public void testReadMutations() throws Exception {
		char delimiter1 = '@';
		char delimiter2 = 'X';
		String readString = delimiter1 + "xyz" + delimiter1 + "a b c" + "\n" + delimiter2 + "1" + delimiter2 + "2";
		StringReader aStringReader = new StringReader(readString);

		final MutationMaker mockMutationMaker = context.mock(MutationMaker.class);
		context.checking(new Expectations(){{
			one(mockMutationMaker).mutate("xyz", "a b c");
			one(mockMutationMaker).mutate("1", "2");
		}});

		RealMutationsList aMutationsList = new RealMutationsList("", NullErrorStream);
		aMutationsList.visit(new BufferedReader(aStringReader), mockMutationMaker);

		context.assertIsSatisfied();
	}

	public void testReadMutationsIgnoreBogusLinesIncludingBlankOnes() throws Exception {
		char delimiter1 = '@';
		char delimiter2 = 'X';
		String readString = delimiter1 + "xyz" + delimiter1 + "\n" + // bogus
																		// line
				delimiter2 + "1" + delimiter2 + "2" + delimiter2 + "3" + "\n" + // will
																				// ignore
																				// too
																				// many
				"\n" + // will ignore blank lines
				delimiter2 + "a" + delimiter2 + "b" + delimiter2; // valid
																	// line
		StringReader aStringReader = new StringReader(readString);

		final MutationMaker mockMutationMaker = context.mock(MutationMaker.class);
		context.checking(new Expectations(){{
			one(mockMutationMaker).mutate("1", "2");
			one(mockMutationMaker).mutate("a", "b");
		}});

		RealMutationsList aMutationsList = new RealMutationsList("", NullErrorStream);
		aMutationsList.visit(new BufferedReader(aStringReader), mockMutationMaker);

		context.assertIsSatisfied();
	}
}