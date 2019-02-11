package week1.soojendus;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Exercise3Test {

    @Test
    public void createEnv() {
        List<String> args = Arrays.asList("x", "1", "y", "12", "z", "5");
        Map<String, Integer> env = Exercise3.createMap(args);
        assertEquals(1, env.get("x").intValue());
        assertEquals(5, env.get("z").intValue());
        assertEquals(12, env.get("y").intValue());
    }

    @Test
    public void eval() {
        assertEquals(1, Exercise3.eval("1"));
        assertEquals(13, Exercise3.eval("2+3+8"));

    }

}