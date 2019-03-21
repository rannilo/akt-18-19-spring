package week4;

import java.io.IOException;
import java.util.*;

public class FiniteAutomaton extends AbstractAutomaton {
    private Map<Integer, Map<Character, Set<Integer>>> transitions = new HashMap<>();
    private Integer startState = null;
    private Set<Integer> acceptingStates = new HashSet<>();
    private Set<Integer> states = new HashSet<>();

    @Override
    public void addState(Integer state, boolean isAcceptingState) {
        states.add(state);
        transitions.put(state, new HashMap<>());
        if (isAcceptingState) acceptingStates.add(state);
    }

    @Override
    public void setStartState(Integer state) {
        this.startState = state;
    }

    @Override
    public void addTransition(Integer fromState, Character label, Integer toState) {
        Map<Character, Set<Integer>> state = transitions.get(fromState);
        Set<Integer> dest = state.computeIfAbsent(label, k -> new HashSet<>());
        dest.add(toState);
    }

    @Override
    public Set<Integer> getStates() {
        return states;
    }

    @Override
    public Integer getStartState() {
        return startState;
    }

    @Override
    public boolean isAcceptingState(Integer state) {
        return acceptingStates.contains(state);
    }

    @Override
    public Set<Character> getDefinedInputs(Integer state) {
        return transitions.get(state).keySet();
    }

    @Override
    public Set<Integer> getDestinations(Integer state, Character input) {
        return transitions.get(state).getOrDefault(input, Collections.emptySet());
    }

    @Override
    public boolean accepts(String input) {
        Set<Integer> currentStates = epsilonClosure(Collections.singleton(startState));
        for (char c : input.toCharArray()) {
            if (currentStates.isEmpty()) return false;
            currentStates = move(currentStates, c);
        }
        return !Collections.disjoint(currentStates, acceptingStates);
    }

    // Defineerime abifunktsioon step, millega saab nii sulund kui ka move defineerida.
    // See vaatab, kuhu võib etteantud tähega (muuhulgas epsiloniga) minna.
    private Set<Integer> step(Set<Integer> states, Character c) {
        Set<Integer> nextState = new HashSet<>();
        for (Integer state : states) nextState.addAll(getDestinations(state, c));
        return nextState;
    }

    public Set<Integer> epsilonClosure(Set<Integer> states) {
        Set<Integer> closure = new HashSet<>();
        while (closure.addAll(states)) states = step(states, null);
        return closure;
        //return Fixpoints.closure(x -> step(x, null), states);
    }

    public Set<Integer> move(Set<Integer> states, Character c) {
        return epsilonClosure(step(states, c));
    }

    /**
     * Seda meetodit ei hinnata ja seda ei pea muutma, aga läbikukkunud testide korral
     * antakse sulle automaadi kirjelduseks just selle meetodi tagastusväärtus.
     */
    @Override
    public String toString() {
        return "trans: " + transitions + "\n" +
                "start: " + startState + ", end: " + acceptingStates;
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
