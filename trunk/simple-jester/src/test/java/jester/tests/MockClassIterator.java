package jester.tests;

import jester.*;
import junit.framework.Assert;

public class MockClassIterator implements ClassIterator {
	private int expectedIterateCalls = 0;
	private int actualIterateCalls = 0;

	private ClassTestTester expectedClassTestTester;
	public MockClassIterator() {
		super();
	}

	public void setExpectedIterate(ClassTestTester classTestTester) {
		expectedClassTestTester = classTestTester;
	}
	public void setExpectedIterateCalls(int calls) {
		expectedIterateCalls = calls;
	}
	public void verify() {
		Assert.assertEquals(expectedIterateCalls, actualIterateCalls);
	}

	public void iterate(ClassTestTester visitor) {
		actualIterateCalls++;
		Assert.assertEquals(expectedClassTestTester, visitor);
	}
}