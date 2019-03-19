package week5.basics;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week5.regex.RegexNode;
import week5.regex.RegexParser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegexAnalyzerTest {


    @Test
    public void test01_matchesEmptyWordOperators() {
        checkMatchesEmpty("ε", true);
        checkMatchesEmpty("a", false);
        checkMatchesEmpty("ε|a", true);
        checkMatchesEmpty("εa", false);
        checkMatchesEmpty("a*", true);
    }

    @Test
    public void test02_matchesEmptyWordSimpleCombinations() {
        checkMatchesEmpty("a*|ε", true);
        checkMatchesEmpty("aε*|a", false);
        checkMatchesEmpty("(a|(b|ε))*", true);
        checkMatchesEmpty("εεε", true);
    }

    @Test
    public void test03_matchesInfOperators() {
        checkMatchesInf("ε", false);
        checkMatchesInf("a", false);
        checkMatchesInf("a|b", false);
        checkMatchesInf("ab", false);
        checkMatchesInf("a*", true);
    }

    @Test
    public void test04_matchesInfSimpleCombinations() {
        checkMatchesInf("aa*", true);
        checkMatchesInf("(a|b)*", true);
        checkMatchesInf("(a|(b|ε))*", true);
        checkMatchesInf("ab|a", false);
        checkMatchesInf("a(b|a)ε", false);
    }


    @Test
    public void test05_matchesInfEmptyStar() {
        checkMatchesInf("ε*", false);
        checkMatchesInf("a*", true);
        checkMatchesInf("(ε|ε)*", false);
        checkMatchesInf("ab|ε*", false);
        checkMatchesInf("(εε)*", false);
        checkMatchesInf("(ab|ε)*", true);
    }

    @Test
    public void test06_getAllWordsAtoms() {
        checkFinite("ε", "");
        checkFinite("a", "a");
        checkInfinite("a*");
    }

    @Test
    public void test07_getAllWordsAlt() {
        checkFinite("ε", "");
        checkFinite("a|b", "a", "b");
        checkFinite("a|b|c", "a", "b", "c");
        checkFinite("a|b|ε", "a", "b", "");
        checkFinite("a|b|a", "a", "b");
        checkFinite("a|a|a", "a");
        checkFinite("a|(b|a)|b", "a", "b");
        checkInfinite("(a|b)*");
        checkInfinite("(a|b*)");
    }

    @Test
    public void test08_getAllWordsConcatSingleton() {
        checkFinite("ab", "ab");
        checkFinite("abcdefg", "abcdefg");
        checkInfinite("(ab)*");
    }

    @Test
    public void test09_getAllWordsConcatSets() {
        checkFinite("(a|b)(c|d)", "ac", "ad", "bc", "bd");

        checkFinite("(kala|äri)(mees|naine)",
                "kalamees", "kalanaine",
                "ärimees", "ärinaine");
    }

    @Test
    public void test10_getAllWordsEdgecases() {
        checkFinite("(a|b)(c|d)(e|f|g)",
                "ace", "acf", "acg",
                "ade", "adf", "adg",
                "bce", "bcf", "bcg",
                "bde", "bdf", "bdg");

        checkFinite("(kala|äri)(mees|naine|ε)",
                "kalamees", "kalanaine", "kala",
                "ärimees", "ärinaine", "äri");

        checkFinite("ε*", "");
        checkFinite("((ε*)*)*", "");
    }


    private void checkMatchesEmpty(String originalRegex, boolean expected) {

        RegexNode originalTree = RegexParser.parse(originalRegex);
        boolean actualResult = RegexAnalyzer.matchesEmptyWord(originalTree);

        assertEquals(originalRegex + " oodatud: " + expected, expected, actualResult);
    }

    private void checkMatchesInf(String originalRegex, boolean expected) {

        RegexNode originalTree = RegexParser.parse(originalRegex);
        boolean actualResult = RegexAnalyzer.matchesInfManyWords(originalTree);

        assertEquals(originalRegex + " oodatud: " + expected, expected, actualResult);
    }

    private void checkInfinite(String regex) {
        try {
            RegexAnalyzer.getAllWords(RegexParser.parse(regex));
            fail("Should have thrown exception for infinite language");
        }
        catch (Exception e) {
            // OK
        }
    }

    private void checkFinite(String regex, String... expectedWordsArray) {
        RegexNode tree = RegexParser.parse(regex);
        Set<String> expectedWords = new HashSet<>(Arrays.asList(expectedWordsArray));
        Set<String> actualWords = null;
        try {
            actualWords = RegexAnalyzer.getAllWords(tree);
        } catch (Exception e) {
            fail("Ei oleks tohtinud erindi viskama. Sisend: " + regex);
        }
        assertEquals(expectedWords, actualWords);
    }
}
