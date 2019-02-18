package week2.intro;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IntroTest {


    private List<String> strings;

    @Before
    public void setUpInput() throws IOException {
        strings = Files.readAllLines(Paths.get("input2.txt"));
    }

    @Test
    public void test01() throws Exception {
        check(Exercises.RE1,
                "Mull-mull-mull-mull, väiksed kalad",
                "kus on teie väiksed jalad?");
    }

    @Test
    public void test02() throws Exception {
        check(Exercises.RE2,
                "Mull-mull-mull-mull, väiksed kalad",
                "kus on teie väiksed jalad?",
                "Aastavahetuse magusad palad");
    }

    @Test
    public void test03() throws Exception {
        check(Exercises.RE3,
                "This color is green.",
                "This colour is red.");
    }

    @Test
    public void test04() throws Exception {
        check(Exercises.RE4,
                "jahaaaaaaaaaaaaa!",
                "jaha",
                "jaahaa");
    }

    @Test
    public void test05() throws Exception {
        check(Exercises.RE5,
                "0100010",
                "011110110",
                "100",
                "",
                "1",
                "101");
    }

    @Test
    public void test06() throws Exception {
        check(Exercises.RE6,
                "abab",
                "aaaa",
                "ababab",
                "baab");
    }

    @Test
    public void test07() throws Exception {
        check(Exercises.RE7,
                "abab",
                "aaaa",
                "baba",
                "haha");
    }

    @Test
    public void test08() throws Exception {
        checkReplace(Exercises.RE8, Exercises.RP8,
                "Aavik, Johannes",
                "Adamson, Hendrik",
                "Adson, Artur",
                "Afanasjev, Vahur",
                "Alavainu, Ave",
                "Alle, August",
                "Alliksaar, Artur",
                "Alver, Betti",
                "Arder, Ott");
    }


    @Test
    public void test09() throws Exception {
        checkReplace(Exercises.RE9, "",
                "This is important.");
    }

    private void checkReplace(String re, String rp, String... expected) {
        List<String> result = Util.replace(re, rp, strings);
        assertEquals(Arrays.asList(expected), result);
    }

    private void check(String re, String... expected) {
        List<String> result = Util.grep(re, strings);
        assertEquals(Arrays.asList(expected), result);
    }

}