package jester.tests;

import jester.MutationMaker;

import com.mockobjects.ExpectationCounter;
import com.mockobjects.ExpectationList;

public class MockMutationMaker implements MutationMaker {
	private ExpectationCounter myMutateCalls = new ExpectationCounter("jester.MutationMaker MutateCalls");
	private ExpectationList myMutateParameter0Values = new ExpectationList("jester.MutationMaker MutateParameter0Values");
	private ExpectationList myMutateParameter1Values = new ExpectationList("jester.MutationMaker MutateParameter1Values");

	/**
	 * MockMutationMaker constructor comment.
	 */
	public MockMutationMaker() {
		super();
	}

	public void addExpectedMutateValues(String arg0, String arg1) {
		myMutateParameter0Values.addExpected(arg0);
		myMutateParameter1Values.addExpected(arg1);
	}

	public void mutate(String arg0, String arg1) {
		myMutateCalls.inc();
		myMutateParameter0Values.addActual(arg0);
		myMutateParameter1Values.addActual(arg1);
	}

	public void setExpectedMutateCalls(int calls) {
		myMutateCalls.setExpected(calls);
	}

	public void verify() {
		myMutateCalls.verify();
		myMutateParameter0Values.verify();
		myMutateParameter1Values.verify();
	}
}