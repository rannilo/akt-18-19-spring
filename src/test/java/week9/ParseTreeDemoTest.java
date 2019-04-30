package week9;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseTreeDemoTest {
    @Test
    public void evaluate() throws Exception {
        assertEquals(12, ParseTreeDemo.evaluate("5 + 10 - 3"));
        assertEquals(2, ParseTreeDemo.evaluate("5 - 2 - 1"));
    }
}
