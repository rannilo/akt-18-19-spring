package week4;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;

/**
 * Abstraktne klass FiniteAutomaton implementeerimiseks.
 * Võimaldab automaadi joonistamist.
 */
public abstract class AbstractAutomaton {
    /**
     * Selle meetodiga annab automaadi koostaja teada, millised olekud automaadis
     * esinevad. isAcceptingState ütleb, kas tegemist on lõppolekuga.
     */
    public abstract void addState(Integer state, boolean isAcceptingState);

    /**
     * Kasulik abimeetod 5. kodutööks, mis loob uue seisundi ja tagastab selle ID.
     */
    public int addState(boolean isAcceptingState) {
        Set<Integer> states = getStates();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (!states.contains(i)) {
                addState(i, isAcceptingState);
                return i;
            }
        }
        throw new RuntimeException("Too many states");
    }

    /**
     * Selle meetodiga määratakse algolek. Võib eeldada, et eelnevalt on see olek
     * automaati lisatud.
     */
    public abstract void setStartState(Integer state);

    /**
     * Selle meetodiga lisatakse uus üleminek. Epsilon-ülemineku korral on label==null.
     * Võib eeldada, et olekud fromState ja toState on juba eelnevalt lisatud.
     */
    public abstract void addTransition(Integer fromState, Character label, Integer toState);

    /**
     * Tagastab kõigi lisatud olekute hulga.
     */
    public abstract Set<Integer> getStates();

    /**
     * Tagastab algoleku.
     */
    public abstract Integer getStartState();

    /**
     * Tagastab, kas antud olek on lõppolek.
     */
    public abstract boolean isAcceptingState(Integer state);

    /**
     * Tagastab, millised üleminekud antud olekust väljuvad.
     */
    public abstract Set<Character> getDefinedInputs(Integer state);

    /**
     * Tagastab, millistesse olekutesse antud olekust antud sümboliga saab.
     */
    public abstract Set<Integer> getDestinations(Integer state, Character input);

    /**
     * See meetod peab ütlema, kas automaat tunneb ära näidatud sisendi.
     */
    public abstract boolean accepts(String input);

    private String toDot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph DFA {\n");
        sb.append("rankdir=LR\n");
        sb.append("bgcolor=transparent;\n");
        sb.append("null [shape = plaintext label=\"\"]\n");
        sb.append("node [style=filled, shape=circle, fixedsize=true];\n");
        sb.append("null -> ").append(getStartState()).append('\n');

        Set<Integer> states = getStates();
        for (Integer state : states) {
            sb.append(state);
            sb.append(String.format(" [label=<%d>", state));
            if (isAcceptingState(state)) sb.append(", shape=doublecircle");
            sb.append("]\n");
        }

        for (Integer state : states) {
            for (Character label : getDefinedInputs(state)) {
                for (Integer dest : getDestinations(state, label)) {
                    if (label == null) label = 'ε';
                    sb.append(String.format("%d -> %d [label=\"%s\"]\n", state, dest, label));
                }
            }
        }

        sb.append("}");
        return sb.toString();
    }

    public void createDotFile(String fileName) throws IOException {
        String dotString = toDot();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (ext.equals("dot")) Files.write(Paths.get(fileName), dotString.getBytes(), StandardOpenOption.CREATE);
        else Graphviz.fromString(dotString).render(Format.PNG).toFile(new File(fileName).getCanonicalFile());
    }

    @Override
    public String toString(){
        return toDot();
    }
}
