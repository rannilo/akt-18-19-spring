package week5;

import week4.FiniteAutomaton;
import week5.regex.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Grep {
    /*
     * main meetodit ei ole vaja muuta.
     *
     * See meetod on siin vaid selleks, et anda käesolevale  harjutusele veidi
     * realistlikum kontekst. Aga tegelikult on see vaid mäng -- see programm ei
     * pretendeeri päeva kasulikuima programmi tiitlile. Päris elus kasuta päris grep-i.
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 1 || args.length > 2) {
            System.err.println(
                    "Programm vajab vähemalt ühte argumenti: regulaaravaldist.\n" +
                            "Teiseks argumendiks võib anda failinime (kui see puudub, siis loetakse tekst standardsisendist).\n" +
                            "Failinime andmisel eeldatakse, et tegemist on UTF-8 kodeeringus tekstifailiga.\n" +
                            "Rohkem argumente programm ei aktsepteeri.\n"
            );
            System.exit(1);
        }

        RegexNode regex = RegexParser.parse(args[0]);
        FiniteAutomaton automaton = optimize(regexToFiniteAutomaton(regex));

        Scanner scanner;
        if (args.length == 2) {
            scanner = new Scanner(new FileInputStream(args[1]), "UTF-8");
        } else {
            scanner = new Scanner(System.in);
        }

        // kuva ekraanile need read, mis vastavad antud regulaaravaldisele/automaadile
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (automaton.accepts(line)) {
                System.out.println(line);
            }
        }

        scanner.close();
    }

    /*
     * See meetod peab loenguslaididel toodud konstruktsiooni põhjal koostama ja tagastama
     * etteantud regulaaravaldisele vastava mittedetermineeritud lõpliku automaadi.
     * Selle meetodi korrektne implementeerimine on antud ülesande juures kõige tähtsam.
     *
     * (Sa võid selle meetodi implementeerimiseks kasutada abimeetodeid ja ka abiklasse,
     * aga ära muuda meetodi signatuuri, sest automaattestid eeldavad just sellise signatuuri
     * olemasolu.)
     *
     * (Selle ülesande juures pole põhjust kasutada vahetulemuste salvestamiseks klassivälju,
     * aga kui sa seda siiski teed, siis kontrolli, et see meetod töötab korrektselt ka siis,
     * kui teda kutsutakse välja mitu korda järjest.)
     */
    public static FiniteAutomaton regexToFiniteAutomaton(RegexNode regex) {
        FiniteAutomaton automaton = new FiniteAutomaton();
        Fragment fragment = new BookVisitor(automaton).visit(regex);
        automaton.setStartState(fragment.in);
        int endState = automaton.addState(true);
        automaton.addTransition(fragment.out, fragment.outLabel, endState);
        return automaton;
    }

    private static class Fragment {
        public final int in;
        public final int out;
        public final Character outLabel; // label for transition from out

        private Fragment(int in, int out, Character outLabel) {
            this.in = in;
            this.out = out;
            this.outLabel = outLabel;
        }
    }

    private static class BookVisitor extends RegexVisitor<Fragment> {
        private final FiniteAutomaton automaton;

        private BookVisitor(FiniteAutomaton automaton) {
            this.automaton = automaton;
        }

        @Override
        protected Fragment visit(Alternation alternation) {
            Fragment leftFragment = visit(alternation.getLeft());
            Fragment rightFragment = visit(alternation.getRight());
            int in = automaton.addState(false);
            int out = automaton.addState(false);
            automaton.addTransition(in, null, leftFragment.in);
            automaton.addTransition(in, null, rightFragment.in);
            automaton.addTransition(leftFragment.out, leftFragment.outLabel, out);
            automaton.addTransition(rightFragment.out, rightFragment.outLabel, out);
            return new Fragment(in, out, null);
        }

        @Override
        protected Fragment visit(Concatenation concatenation) {
            Fragment leftFragment = visit(concatenation.getLeft());
            Fragment rightFragment = visit(concatenation.getRight());
            automaton.addTransition(leftFragment.out, leftFragment.outLabel, rightFragment.in);
            return new Fragment(leftFragment.in, rightFragment.out, rightFragment.outLabel);
        }

        @Override
        protected Fragment visit(Epsilon epsilon) {
            int state = automaton.addState(false);
            return new Fragment(state, state, null);
        }

        @Override
        protected Fragment visit(Letter letter) {
            int state = automaton.addState(false);
            return new Fragment(state, state, letter.getSymbol());
        }

        @Override
        protected Fragment visit(Repetition repetition) {
            Fragment childFragment = visit(repetition.getChild());
            int state = automaton.addState(false);
            automaton.addTransition(state, null, childFragment.in);
            automaton.addTransition(childFragment.out, childFragment.outLabel, state);
            return new Fragment(state, state, null);
        }
    }

    /** See meetod peab looma etteantud NFA-le vastava DFA, st. etteantud
     *  automaat tuleb determineerida.
     *  Kui sa seda ei jõua teha, siis jäta see meetod nii, nagu ta on.
     */
    public static FiniteAutomaton optimize(FiniteAutomaton nfa) {
        FiniteAutomaton dfa = new FiniteAutomaton();
        Map<Set<Integer>, Integer> nfa2dfa = new HashMap<>();
        Set<Set<Integer>> U = new HashSet<>();
        // Algoleku lisamine:
        Set<Integer> Q0 = nfa.epsilonClosure(Collections.singleton(nfa.getStartState()));
        int q0 = addNfaStatesetToDfa(nfa, Q0, dfa);
        nfa2dfa.put(Q0, q0);
        dfa.setStartState(q0);
        U.add(Q0);
        while (!U.isEmpty()) {
            Set<Integer> S = U.iterator().next();
            U.remove(S);
            // Muutuja nimed on nagu Varmo slaides, aga teoorias on DFA seisundiks lihtsalt NFA seisundite hulgad.
            // Meie tähistame väikse tähega NFA seisundi hulgale vastav päris DFA seisund, mis on lihtsalt täisarv:
            Integer s = nfa2dfa.get(S);
            Set<Character> sigma = getOutgoingLabels(nfa, S);
            for (char a : sigma) {
                Set<Integer> T = nfa.move(S, a);
                Integer t = nfa2dfa.computeIfAbsent(T, key -> {
                    U.add(T);
                    return addNfaStatesetToDfa(nfa, T, dfa);
                });
                dfa.addTransition(s, a, t);
            }
        }
        return dfa;
    }

    private static int addNfaStatesetToDfa(FiniteAutomaton nfa, Set<Integer> S, FiniteAutomaton dfa) {
        return dfa.addState(S.stream().anyMatch(nfa::isAcceptingState));
    }

    private static Set<Character> getOutgoingLabels(FiniteAutomaton nfa, Set<Integer> S) {
        // Tahaks ju kirjutada S.flatMap(nfa.getDefinedInputs(_)), aga Javas on parem nii:
        Set<Character> result = new HashSet<>();
        for (Integer state : S) result.addAll(nfa.getDefinedInputs(state));
        result.remove(null); // Ja siin me kindlasti ei taha epsilon-servasid vaadata!
        return result;
    }

}
