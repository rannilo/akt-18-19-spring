package week5;

import week4.FiniteAutomaton;
import week5.regex.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Grep {
    /*
     * main meetodit ei ole vaja muuta.
     *
     * See meetod on siin vaid selleks, et anda käesolevale  harjutusele veidi
     * realistlikum kontekst. Aga tegelikult on see vaid mäng -- see programm ei
     * pretendeeri päeva kasulikuima programmi tiitlile. Päris elus kasuta päris grep-i.
     */
    public static void main(String[] args) throws IOException {

        FiniteAutomaton finiteAutomaton = regexToFiniteAutomaton(RegexParser.parse("a|b"));
        System.out.println(finiteAutomaton);

        if (args.length < 1 || args.length > 2) {
           /* System.err.println(
                    "Programm vajab vähemalt ühte argumenti: regulaaravaldist.\n" +
                            "Teiseks argumendiks võib anda failinime (kui see puudub, siis loetakse tekst standardsisendist).\n" +
                            "Failinime andmisel eeldatakse, et tegemist on UTF-8 kodeeringus tekstifailiga.\n" +
                            "Rohkem argumente programm ei aktsepteeri.\n"
            );*/
            System.exit(1);
        }

        RegexNode regex = RegexParser.parse(args[0]);
        System.out.println(regex.toString());
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
        /**
         * Algolek on alati 1
         * Lõppolek on alati suurima numbriga olek
         * Vahepealsed numbrid on kõik esindatud
         * Kasutatud on Thompsoni konstruktsioon
         */
        RegexVisitor<FiniteAutomaton> visitor = new RegexVisitor<FiniteAutomaton>() {
            @Override
            protected FiniteAutomaton visit(Letter letter) {
                FiniteAutomaton finiteAutomaton = new FiniteAutomaton();
                finiteAutomaton.addState(1, false);
                finiteAutomaton.addState(2, true);
                finiteAutomaton.setStartState(1);
                finiteAutomaton.addTransition(1, letter.getSymbol(), 2);
                return finiteAutomaton;
            }

            @Override
            protected FiniteAutomaton visit(Epsilon epsilon) {
                FiniteAutomaton finiteAutomaton = new FiniteAutomaton();
                finiteAutomaton.addState(1, false);
                finiteAutomaton.addState(2, true);
                finiteAutomaton.setStartState(1);
                finiteAutomaton.addTransition(1, null, 2);
                return finiteAutomaton;
            }

            @Override
            protected FiniteAutomaton visit(Repetition repetition) {
                FiniteAutomaton finiteAutomaton = new FiniteAutomaton();
                FiniteAutomaton child = visit(repetition.getChild());
                Integer acceptingState = child.getStates().size() + 2;
                Integer startState = 1;

                //Juurde tulevad olekud ja üleminekud
                finiteAutomaton.addState(startState, false);
                finiteAutomaton.setStartState(startState);
                finiteAutomaton.addState(acceptingState, true);
                finiteAutomaton.addTransition(startState, null, acceptingState);
                Integer childStartState = child.getStartState();
                finiteAutomaton.addTransition(startState, null, childStartState + 1);

                //Lisan lapse olekud
                for (Integer fromState : child.getStates()) {
                    finiteAutomaton.addState(fromState + 1, false);
                    if (child.isAcceptingState(fromState)) { //Lapse lõppolekust peab saama lapse algusesse ja kogu automaadi lõppu
                        finiteAutomaton.addTransition(fromState + 1, null, childStartState + 1);
                        finiteAutomaton.addTransition(fromState + 1, null, acceptingState);
                    }

                    //Lisan lapse üleminekud
                    for (Character character : child.getDefinedInputs(fromState)) {
                        for (Integer toState : child.getDestinations(fromState, character)) {
                            finiteAutomaton.addTransition(fromState + 1, character, toState + 1);
                        }
                    }
                }
                return finiteAutomaton;
            }

            /**
             * CONCATENATION
             * @param concatenation regex
             * @return finite automaton
             */
            @Override
            protected FiniteAutomaton visit(Concatenation concatenation) {
                FiniteAutomaton finiteAutomaton = new FiniteAutomaton();
                FiniteAutomaton left = visit(concatenation.getLeft());
                int rightStatesCountStart = left.getStates().size() - 1;
                FiniteAutomaton right = visit(concatenation.getRight());

                Integer acceptingState = null;
                Integer startState = left.getStartState();

                //Vasakpoolse automaadi töötlemine
                for (Integer state : left.getStates()) {
                    //Lisan olekud
                    finiteAutomaton.addState(state, false);

                    //Lisan üleminekud
                    Set<Character> definedInputs = left.getDefinedInputs(state);
                    for (Character character : definedInputs) {
                        Set<Integer> destinations = left.getDestinations(state, character);
                        for (Integer destination : destinations) {
                            finiteAutomaton.addTransition(state, character, destination);
                        }
                    }
                    if (left.isAcceptingState(state)) acceptingState = state;
                }
                finiteAutomaton.setStartState(startState);

                Integer rightStartState = right.getStartState();
                //Parempoolse automaadi töötlemine
                for (Integer state : right.getStates()) {

                    //Lisan olekud
                    if (!state.equals(rightStartState)) {
                        finiteAutomaton.addState(state + rightStatesCountStart, right.isAcceptingState(state));
                    }

                    //Lisan üleminekud
                    Set<Character> definedInputs = right.getDefinedInputs(state);
                    for (Character character : definedInputs) {
                        Set<Integer> destinations = right.getDestinations(state, character);
                        for (Integer destination : destinations) {
                            if (state.equals(rightStartState)) {
                                finiteAutomaton.addTransition(acceptingState, character, destination + rightStatesCountStart);
                            } else {
                                finiteAutomaton.addTransition(state + rightStatesCountStart, character, destination + rightStatesCountStart);
                            }
                        }
                    }
                }

                return finiteAutomaton;
            }

            /**
             * ALTERATION
             * @param alternation regex
             * @return Finite automaton
             */
            @Override
            protected FiniteAutomaton visit(Alternation alternation) {
                FiniteAutomaton finiteAutomaton = new FiniteAutomaton();
                FiniteAutomaton left = visit(alternation.getLeft());
                int rightStatesCountStart = left.getStates().size() - 2;
                FiniteAutomaton right = visit(alternation.getRight());
                int allStatesCount = left.getStates().size() + right.getStates().size();

                Integer acceptingState = allStatesCount - 2;
                Integer startState = left.getStartState();
                finiteAutomaton.setStartState(startState);

                //Vasakpoolse automaadi töötlemine
                for (Integer state : left.getStates()) {
                    //Lisan olekud
                    if (left.isAcceptingState(state)) {
                        finiteAutomaton.addState(acceptingState, true);
                    } else {
                        finiteAutomaton.addState(state, false);
                    }
                    //Lisan üleminekud
                    Set<Character> definedInputs = left.getDefinedInputs(state);
                    for (Character character : definedInputs) {
                        Set<Integer> destinations = left.getDestinations(state, character);
                        for (Integer destination : destinations) {
                            if (left.isAcceptingState(destination)) {
                                finiteAutomaton.addTransition(state, character, acceptingState);
                            } else {
                                finiteAutomaton.addTransition(state, character, destination);
                            }
                        }
                    }
                }

                //Parempoolse automaadi töötlemine
                for (Integer fromState : right.getStates()) {
                    //Lisan olekud
                    if (!fromState.equals(right.getStartState()) && !right.isAcceptingState(fromState)) {
                        finiteAutomaton.addState(fromState + rightStatesCountStart, false);
                    }
                    //Lisan üleminekud
                    Set<Character> definedInputs = right.getDefinedInputs(fromState);
                    for (Character character : definedInputs) {
                        Set<Integer> destinations = right.getDestinations(fromState, character);
                        for (Integer toState : destinations) {
                            if (fromState.equals(startState)) {
                                if (right.isAcceptingState(toState)) {
                                    finiteAutomaton.addTransition(fromState, character, acceptingState);
                                } else {
                                    finiteAutomaton.addTransition(startState, character, toState + rightStatesCountStart);
                                }
                            } else if (right.isAcceptingState(toState)) {
                                finiteAutomaton.addTransition(fromState + rightStatesCountStart, character, acceptingState);
                            } else {
                                finiteAutomaton.addTransition(fromState + rightStatesCountStart, character, toState + rightStatesCountStart);
                            }
                        }
                    }
                }
                return finiteAutomaton;
            }
        };
        return visitor.visit(regex);
    }

    /**
     * See meetod peab looma etteantud NFA-le vastava DFA, st. etteantud
     * automaat tuleb determineerida.
     * Kui sa seda ei jõua teha, siis jäta see meetod nii, nagu ta on.
     */
    public static FiniteAutomaton optimize(FiniteAutomaton nfa) {
        FiniteAutomaton dfa = new FiniteAutomaton();
        Map<Set<Integer>, Integer> NFAStateToDFAState = new HashMap<>();

        Set<Character> sigma = nfa.getAlphabet();
        Set<Set<Integer>> S1 = new HashSet<>();
        Deque<Set<Integer>> W = new ArrayDeque<>();
        Set<Integer> S0 = nfa.epsilonClosure(Collections.singleton(nfa.getStartState()));
        W.push(S0);
        NFAStateToDFAState.put(S0, 1);
        dfa.setStartState(1);
        int count = 2;
        while (!W.isEmpty()) {
            Set<Integer> X = W.pop();
            S1.add(X);
            if (!NFAStateToDFAState.containsKey(X)) {
                NFAStateToDFAState.put(X, count);
                count += 1;
            }
            for (Character a : sigma) {
                Set<Integer> Y = nfa.move(X, a);
                if (!W.contains(Y) && !S1.contains(Y)) {
                    if (!NFAStateToDFAState.containsKey(Y)) {
                        NFAStateToDFAState.put(Y, count);
                        count += 1;
                    }
                    W.push(Y);
                }
                dfa.addTransition(NFAStateToDFAState.get(X), a, NFAStateToDFAState.get(Y));
            }
        }
        //Lisan olekud
        for (Set<Integer> nfaStateSet : S1) {
            Integer dfaState = NFAStateToDFAState.get(nfaStateSet);
            Boolean isAccepting = false;
            for (Integer state : nfaStateSet) {
                System.out.println("State: " + state);
                if (nfa.isAcceptingState(state)) {
                    System.out.println("IS ACCEPTING");
                    isAccepting = true;
                    break;
                }
            }
            dfa.addState(dfaState, isAccepting);
        }
        return dfa;
    }
}
