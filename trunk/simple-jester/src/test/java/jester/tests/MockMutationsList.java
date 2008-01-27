package jester.tests;

import jester.MutationMaker;
import jester.MutationsList;

import com.mockobjects.ExpectationCounter;
import com.mockobjects.ExpectationList;

public class MockMutationsList implements MutationsList {
	private ExpectationCounter myVisitCalls = new ExpectationCounter("jester.MutationsList VisitCalls");
	private ExpectationList myVisitParameter0Values = new ExpectationList("jester.MutationsList VisitParameter0Values");
	/**
	 * MockMutationsList constructor comment.
	 */
	public MockMutationsList() {
		super();
	}
	public void addExpectedVisitValues(MutationMaker arg0) {
		myVisitParameter0Values.addExpected(arg0);
	}
	public void setExpectedVisitCalls(int calls) {
		myVisitCalls.setExpected(calls);
	}
	public void verify() {
		myVisitCalls.verify();
		myVisitParameter0Values.verify();
	}
	public void visit(MutationMaker arg0) {
		myVisitCalls.inc();
		myVisitParameter0Values.addActual(arg0);
	}
}