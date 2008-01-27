package jester.acceptancetests;

public class VeryTestedTest extends junit.framework.TestCase {
    public void testNumber() {
        assertEquals(1, new VeryTested().mustReturnOne());
    }
}