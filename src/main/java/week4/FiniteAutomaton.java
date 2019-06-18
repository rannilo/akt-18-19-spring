package week4;

import javax.print.DocFlavor;
import java.io.IOException;
import java.util.*;

public class FiniteAutomaton extends AbstractAutomaton {
    private Set<Integer> olekud = new HashSet<>();
    private Set<Integer> lõppolekud = new HashSet<>();
    private Integer algolek;
    private Map<Integer, Set<Pair<Character, Integer>>> üleminekud = new HashMap<>();

    @Override
    public void addState(Integer state, boolean isAcceptingState) {
        olekud.add(state);
        if (isAcceptingState) {
            lõppolekud.add(state);
        }
    }

    @Override
    public void setStartState(Integer state) {
        this.algolek = state;
    }

    @Override
    public void addTransition(Integer fromState, Character label, Integer toState) {
        Set<Pair<Character, Integer>> seesmineMap = this.üleminekud.computeIfAbsent(fromState, k -> new HashSet<>());
        seesmineMap.add(new Pair<Character, Integer>(label, toState));
    }

    @Override
    public Set<Integer> getStates() {
        return olekud;
    }

    @Override
    public Integer getStartState() {
        return algolek;
    }

    @Override
    public boolean isAcceptingState(Integer state) {
        return lõppolekud.contains(state);
    }

    @Override
    public Set<Character> getDefinedInputs(Integer state) {
        Set<Character> definedInputs = new HashSet<>();
        for (Pair<Character, Integer> pair : this.üleminekud.getOrDefault(state, new HashSet<>())) {
            definedInputs.add(pair.getKey());
        }
        return definedInputs;
    }

    @Override
    public Set<Integer> getDestinations(Integer state, Character input) {
        /*Set<Integer> destinations = new HashSet<>();
        Set<Integer> epsilonClosure = epsilonClosure(state); //võtan kõigepealt epsilon-sulundi
        epsilonClosure.add(state);
        for (Integer epsilonState : epsilonClosure) { //iga jõutud oleku kohta, vaatan, kuhu saan input characteriga liikuda
            Set<Pair<Character, Integer>> transitions = this.üleminekud.getOrDefault(epsilonState, new HashSet<>());
            for (Pair<Character, Integer> pair : transitions) {
                if (pair.getKey() == input) {
                    destinations.add(pair.getValue());
                    destinations.addAll(epsilonClosure(pair.getValue())); //võtan uuesti epsilon-sulundi
                }
            }
        }*/
        Set<Integer> destinations = new HashSet<>();
        Set<Pair<Character, Integer>> transitions = this.üleminekud.getOrDefault(state, new HashSet<>());
        for (Pair<Character, Integer> transition : transitions) {
            if (transition.getKey() == input) destinations.add(transition.getValue());
        }
        return destinations;
    }

    public Set<Integer> epsilonClosure(Set<Integer> states) { //leiab kõik olekud, kuhu saab sisendiks antud olekust epsilon-üleminekutega jõuda
        Set<Integer> destinations = new HashSet<>();

        for (Integer state : states) {
            Stack<Integer> statesStack = new Stack<>();

            Set<Pair<Character, Integer>> üleminekud = this.üleminekud.getOrDefault(state, new HashSet<>()); //esimene tase
            for (Pair<Character, Integer> pair : üleminekud) {
                if (pair.getKey() == null) {
                    statesStack.push(pair.getValue());
                }
            }
            while (!statesStack.empty()) {
                Integer currentState = statesStack.pop();
                if (!destinations.contains(currentState)) {
                    destinations.add(currentState);
                    Set<Pair<Character, Integer>> üleminekud2 = this.üleminekud.getOrDefault(currentState, new HashSet<>());
                    for (Pair<Character, Integer> pair : üleminekud2) {
                        if (pair.getKey() == null) {
                            statesStack.push(pair.getValue());
                        }
                    }
                }
            }
            destinations.add(state);
        }
        return destinations;
    }

    @Override
    public boolean accepts(String input) {
        Set<Integer> praegusedOlekud = new HashSet<>();
        praegusedOlekud.add(this.algolek);
        praegusedOlekud.addAll(epsilonClosure(praegusedOlekud));

        for (char c : input.toCharArray()) {
            praegusedOlekud = move(praegusedOlekud, c);
        }

        for (Integer olek : praegusedOlekud) {
            if (isAcceptingState(olek)) return true;
        }
        return false;
    }

    /**
     * Seda meetodit ei hinnata ja seda ei pea muutma, aga läbikukkunud testide korral
     * antakse sulle automaadi kirjelduseks just selle meetodi tagastusväärtus.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    public Set<Character> getAlphabet() {
        Set<Character> alphabet = new HashSet<>();
        for (Integer state : this.getStates()) {
            for (Character character : this.getDefinedInputs(state)) {
                alphabet.add(character);
            }
        }
        alphabet.remove(null);
        return alphabet;
    }

    public Set<Integer> move(Set<Integer> x, Character c) {
        Set<Integer> destinations = new HashSet<>();
        for (Integer state : x) {
            destinations.addAll(getDestinations(state, c));
        }
        Set<Integer> epsilonAddedStates = new HashSet<>();
        epsilonAddedStates.addAll(epsilonClosure(destinations));
        destinations.addAll(epsilonAddedStates);
        return destinations;
    }

    public static void main(String[] args) throws IOException {
        FiniteAutomaton fa = new FiniteAutomaton();

        fa.addState(0, false);
        fa.addState(1, true);
        fa.addState(2, false);

        fa.addTransition(0, 'b', 0);
        fa.addTransition(0, 'c', 2);
        fa.addTransition(2, 'a', 1);
        fa.addTransition(1, 'd', 0);
        fa.addTransition(0, null, 1);

        fa.setStartState(0);

        System.out.println(fa.accepts("cadbbbca")); // true
        System.out.println(fa.accepts("abc"));      // false
        System.out.println(fa.accepts(""));         // true

        // Pead ise veenduda, et toString töötab...
        System.out.println(fa);
        fa.createDotFile("auto.png");
    }
}

class Pair<X, Y> {
    public final X x;
    public final Y y;

    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getKey() {
        return x;
    }

    public Y getValue() {
        return y;
    }
}