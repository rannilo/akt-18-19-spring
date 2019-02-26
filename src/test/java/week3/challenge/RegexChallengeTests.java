package week3.challenge;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.RegExp;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegexChallengeTests {

    @Test
    public void testL1() {
        check(AKTRegexChallenge.L1, ProblemSpec.DEFS[0].getAutomaton());
    }

    @Test
    public void testL2() {
        check(AKTRegexChallenge.L2, ProblemSpec.DEFS[1].getAutomaton());
    }

    @Test
    public void testRE1() {
        check(AKTRegexChallenge.RE1, ProblemSpec.DEFS[2].getAutomaton());
    }

    @Test
    public void testRE2() {
        check(AKTRegexChallenge.RE2, ProblemSpec.DEFS[3].getAutomaton());
    }

    @Test
    public void testRE3() {
        check(AKTRegexChallenge.RE3, ProblemSpec.DEFS[4].getAutomaton());
    }

    @Test
    public void testRE4() {
        check(AKTRegexChallenge.RE4, ProblemSpec.DEFS[5].getAutomaton());
    }

    public static void check(String s, Automaton a) {
        Automaton q = (new RegExp(s)).toAutomaton();
        String s1 = BasicOperations.minus(a, q).getShortestExample(true);
        if (s1 != null) fail("Sinu regex ei tunne ära sõna \"" + eps(s1) + "\".");
        String s2 = BasicOperations.minus(q, a).getShortestExample(true);
        if (s2 != null) fail("Sinu regex sobitub ka sõnaga \"" + eps(s2) + "\".");
    }

    private static String eps(String s) {
        if (s.equals("")) return "ε";
        else return s;
    }

}
