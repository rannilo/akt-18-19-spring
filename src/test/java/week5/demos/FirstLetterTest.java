package week5.demos;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;


public class FirstLetterTest {

    @Test
    public void testGetFirst() throws Exception {
        checkFirst("a", 'a');
        checkFirst("ab", 'a');
        checkFirst("a|b", 'a', 'b');
        checkFirst("(a|b)c", 'a', 'b');
        checkFirst("(a|b)*", 'a', 'b');
        checkFirst("(a|b)*c", 'a', 'b', 'c');
        checkFirst("ε(a|b)*c", 'a', 'b', 'c');
    }

    private void checkFirst(String regex, Character... expected) {
        Set<Character> expectedSet = new HashSet<>(Arrays.asList(expected));
        Set<Character> actualSet = FirstLetter.getFirst(regex);
        assertEquals(actualSet, expectedSet);
    }
}