package jester.tests;

import java.io.*;
import java.io.BufferedReader;
import java.io.StringReader;

import jester.MutationsList;
import jester.RealMutationsList;
import junit.framework.*;

public class MutationsListTest extends TestCase {
	private static PrintStream NullErrorStream = new PrintStream(new ByteArrayOutputStream());
	
	public MutationsListTest(String name) {
		super(name);
	}
	public static void main(String args[]) {
		junit.awtui.TestRunner.main(new String[] { "jester.tests.MutationsListTest" });
	}
	public static Test suite() {
		TestSuite suite = new TestSuite(MutationsListTest.class);
		return suite;
	}

	public void testDefaults() throws Exception {
		MockMutationMaker aMockMutationMaker = new MockMutationMaker();

		aMockMutationMaker.addExpectedMutateValues("true", "false");
		aMockMutationMaker.addExpectedMutateValues("false", "true");
		aMockMutationMaker.addExpectedMutateValues("if(", "if(true ||");
		aMockMutationMaker.addExpectedMutateValues("if (", "if (true ||");
		aMockMutationMaker.addExpectedMutateValues("if(", "if(false &&");
		aMockMutationMaker.addExpectedMutateValues("if (", "if (false &&");
		aMockMutationMaker.addExpectedMutateValues("==", "!=");
		aMockMutationMaker.addExpectedMutateValues("!=", "==");

		MutationsList aMutationsList = new RealMutationsList("there must be no file called this", NullErrorStream); //there is no file called "there must be no file called this"
		aMutationsList.visit(aMockMutationMaker);

		aMockMutationMaker.verify();
	}

	public void testReadMutations() throws Exception {
		char delimiter1 = '@';
		char delimiter2 = 'X';
		String readString = delimiter1 + "xyz" + delimiter1 + "a b c" + "\n" + 
							delimiter2 + "1" + delimiter2 + "2";
		StringReader aStringReader = new StringReader(readString);

		MockMutationMaker aMockMutationMaker = new MockMutationMaker();

		aMockMutationMaker.addExpectedMutateValues("xyz", "a b c");
		aMockMutationMaker.addExpectedMutateValues("1", "2");

		RealMutationsList aMutationsList = new RealMutationsList("", NullErrorStream);
		aMutationsList.visit(new BufferedReader(aStringReader), aMockMutationMaker);

		aMockMutationMaker.verify();
	}

	public void testReadMutationsIgnoreBogusLinesIncludingBlankOnes() throws Exception {
		char delimiter1 = '@';
		char delimiter2 = 'X';
		String readString = delimiter1 + "xyz" + delimiter1 + "\n" + //bogus line
							delimiter2 + "1" + delimiter2 + "2" + delimiter2 + "3" + "\n" + //will ignore too many
							"\n" + //will ignore blank lines
							delimiter2 + "a" + delimiter2 + "b" + delimiter2; //valid line
		StringReader aStringReader = new StringReader(readString);

		MockMutationMaker aMockMutationMaker = new MockMutationMaker();

		aMockMutationMaker.addExpectedMutateValues("1", "2");
		aMockMutationMaker.addExpectedMutateValues("a", "b");

		RealMutationsList aMutationsList = new RealMutationsList("", NullErrorStream);
		aMutationsList.visit(new BufferedReader(aStringReader), aMockMutationMaker);

		aMockMutationMaker.verify();
	}
}