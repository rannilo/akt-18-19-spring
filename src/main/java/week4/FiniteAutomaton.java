package week4;

import java.io.IOException;
import java.util.Set;

public class FiniteAutomaton extends AbstractAutomaton {

    @Override
    public void addState(Integer state, boolean isAcceptingState) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStartState(Integer state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addTransition(Integer fromState, Character label, Integer toState) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> getStates() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer getStartState() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAcceptingState(Integer state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Character> getDefinedInputs(Integer state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> getDestinations(Integer state, Character input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean accepts(String input) {
        throw new UnsupportedOperationException();
    }

    /**
     * Seda meetodit ei hinnata ja seda ei pea muutma, aga läbikukkunud testide korral
     * antakse sulle automaadi kirjelduseks just selle meetodi tagastusväärtus.
     */
    @Override
    public String toString() {
        return super.toString();
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
