package week5;

import dk.brics.automaton.Automaton;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.BricsUtils;
import week4.FiniteAutomaton;
import week5.regex.RegexNode;
import week5.regex.RegexParser;

import java.util.Set;

import static org.junit.Assert.fail;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GrepTests {
    public static String lastTestDescription = "";

    @Test
    public void test1_concat() throws Exception {
        checkRegex("abcde");
        checkRegex("abe");
    }

    @Test
    public void test2_alt() throws Exception {
        checkRegex("a|bc");
        checkRegex("a|bc|d");
    }

    @Test
    public void test3_star() throws Exception {
        checkRegex("a*b*");
        checkRegex("a*b");
        checkRegex("a*|b");
    }


    @Test
    public void test4_combos() throws Exception {
        checkRegex("(ab)*");
        checkRegex("(a|b)*");
        checkRegex("(ab|cd)*");
        checkRegex("(ab)*|cd");
        checkRegex("(ab)*|(cd)*");
    }

    @Test
    public void test5_epsilon() throws Exception {
        checkRegex("ε");
        checkRegex("ε*");
        checkRegex("aε");
        checkRegex("εa");
        checkRegex("ε|b");
        checkRegex("εa|b");
        checkRegex("(aε)*");
    }

    @Test
    public void test6_madness() throws Exception {
        checkRegex("(a|b)*b(a|b)");
        checkRegex("((a|b)*b(a|b))*|ab|b*");
        checkRegex("(a|b)*b(a|b)(x|bgg)*g(aεd)*fa(ga|ε)*");
        checkRegex("(aε)*|a*|b*");
        checkRegex("((ε*)*(a*)*)*");
        checkRegex("(((ε|b)*b(ε|ε|ε*|ε|ε))*|ab|b*)*");
    }

    @Test
    public void test7_detAutomata() {

        // Juba determineeritud
        FiniteAutomaton a0 = new FiniteAutomaton();
        int a0s1 = a0.addState(false);
        int a0s2 = a0.addState(true);
        a0.addTransition(a0s1, 'a', a0s2);
        a0.setStartState(a0s1);
        checkDeterminization(a0);

        FiniteAutomaton a1 = new FiniteAutomaton();
        int a1s1 = a1.addState(false);
        int a1s2 = a1.addState(true);
        a1.addTransition(a1s1, 'a', a1s2);
        a1.addTransition(a1s1, null, a1s2);
        a1.setStartState(a1s1);
        checkDeterminization(a1);

        FiniteAutomaton a2 = new FiniteAutomaton();
        int a2s1 = a2.addState(false);
        int a2s2 = a2.addState(true);
        a2.addTransition(a2s1, 'a', a2s2);
        a2.addTransition(a2s1, 'a', a2s1);
        a2.setStartState(a2s1);
        checkDeterminization(a2);

        FiniteAutomaton a3 = new FiniteAutomaton();
        int a3s1 = a3.addState(false);
        int a3s2 = a3.addState(false);
        int a3s3 = a3.addState(true);
        a3.setStartState(a3s1);
        a3.addTransition(a3s1, null, a3s2);
        a3.addTransition(a3s2, null, a3s1);
        a3.addTransition(a3s1, 'a', a3s3);
        a3.addTransition(a3s2, 'b', a3s3);
        checkDeterminization(a3);
    }

    @Test
    public void test8_everything() {
        checkDeterminization("(a|b)*b(a|b)");
        checkDeterminization("ε");
        checkDeterminization("((a|b)*b(a|b))*|ab|b*");
        checkDeterminization("aε|a|b");
        checkDeterminization("(a|b)*b(a|b)(x|bgg)*g(aεd)*fa(ga|ε)*");
    }





    private FiniteAutomaton checkRegex(String re) {
        lastTestDescription = "Regex: " + re;

        RegexNode root = RegexParser.parse(re);

        FiniteAutomaton auto = Grep.regexToFiniteAutomaton(root);
        Automaton actualBrics   = BricsUtils.fromAktAutomaton(auto);
        Automaton expectedBrics = BricsUtils.fromRegex(re);

        if (!actualBrics.equals(expectedBrics)) {
            Automaton diff = expectedBrics.minus(actualBrics);

            if (diff.isEmpty()) {
                diff = actualBrics.minus(expectedBrics);
            }

            fail("Sinu Grep-i poolt koostatud automaat ei anna õiget vastust sisendiga '"
                    + diff.getShortestExample(true) + "'");
        }
        return auto;
    }


    private void checkDeterminization(String re) {
        FiniteAutomaton auto = checkRegex(re);
        checkDeterminization(auto);
    }

    private void checkDeterminization(FiniteAutomaton automaton) {
        lastTestDescription = "Esialgne automaat:\n" + automaton;

        FiniteAutomaton determinized = Grep.optimize(automaton);

        if (determinized == automaton) {
            fail("Grep.optimize tagastas sama automaadi");
        }
        else if (!isDeterministic(determinized)) {
            fail("Grep.optimize tagastas automaadi, mis pole determineeritud:\n" + determinized);
        }
        else {
            Automaton originalBrics = BricsUtils.fromAktAutomaton(automaton);
            Automaton determinizedBrics = BricsUtils.fromAktAutomaton(determinized);
            if (! determinizedBrics.equals(originalBrics)) {
                Automaton diff = originalBrics.minus(determinizedBrics);

                if (diff.isEmpty()) {
                    diff = determinizedBrics.minus(originalBrics);
                }

                fail ("Grep.optimize poolt tagastatud automaat pole esialgsega samaväärne, "
                        + "erinevus tuleb näiteks sisendi '" + diff.getShortestExample(true) + "' korral:\n\n"
                        + determinized);
            }
        }
    }

    public boolean isDeterministic(FiniteAutomaton automaton) {
        for (Integer state : automaton.getStates()) {
            Set<Character> definedInputs = automaton.getDefinedInputs(state);
            if (definedInputs.contains(null)) return false;
            for (Character input : definedInputs) {
                if (automaton.getDestinations(state, input).size() > 1) return false;
            }
        }
        return true;
    }

}
