package week7.kalaparser;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntroParserTest {

    @Test
    public void testParse01() throws Exception {
        acc("ab");
        acc("");
    }

    @Test
    public void testParse02() throws Exception {
        acc("aabb");
        acc("aaabbb");
        rej("bbbbaaaa");
    }


    @Test
    public void testParse03() throws Exception {
        acc("aabb");
        rej("aabbab");
    }

    private static void check(String input, boolean accept) {
        try {
            IntroParser.parse(input);
            assertTrue(input + " ei kuulu tegelikult keelde", accept);
        } catch (Exception e) {
            assertFalse(input + " peaks kuuluma keelde", accept);
        }
    }

    private static void acc(String input) {
        check(input, true);
    }

    private static void rej(String input) {
        check(input, false);
    }
}
