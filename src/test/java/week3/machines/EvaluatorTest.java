package week3.machines;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class EvaluatorTest {

    private HashMap<String, Integer> env;

    @Before
    public void setUp() throws Exception {
        env = new HashMap<>();
        env.put("x", 5);
        env.put("y", -2);
        env.put("z", 10);
    }


    @Test
    public void testTokenize() throws Exception {
        checkTokenize("2+2",      "2", "+", "2");
        checkTokenize("2 -  8",   "2", "-", "8");
        checkTokenize("2 -- 8",   "2", "-", "-", "8");
        checkTokenize("2 - - 8",   "2", "-", "-", "8");
        checkTokenize("2-2+8",    "2", "-", "2", "+", "8");
    }

    private void checkTokenize(String input, String... strings) {
        Assert.assertEquals(Arrays.asList(strings), AktkiMachines.tokenize(input));
    }


    @Test
    public void testCompute() throws Exception {
        checkCompute(4, "2+2");
        checkCompute(0, "2+2-4");
        checkCompute(8, "2+2--4");
    }

    @Test
    public void testComputeWithVars() throws Exception {
        checkCompute(3, "x+y");
        checkCompute(10, "z");
    }

    @Test
    public void testComputeLong1() throws Exception {
        checkCompute(12, "5 + - - + - + + - 7");
        checkCompute(-2, "5 + - - - - + + - 7");
    }

    @Test
    public void testComputeLong2() throws Exception {
        checkCompute(10, "2-2+10");
    }

    private void checkCompute(int result, String input) {
        assertEquals(result, AktkiMachines.compute(AktkiMachines.tokenize(input), env));
    }
}