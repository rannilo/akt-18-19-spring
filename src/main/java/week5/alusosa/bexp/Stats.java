package week5.alusosa.bexp;

import week5.alusosa.bexp.bexpAst.Var;

import java.util.Set;
import java.util.TreeSet;

public final class Stats {

    private Set<Character> variables;
    private boolean found;

    public Stats() {
        variables = new TreeSet<>();
        found = false;
    }

    public Set<Character> getVariables() {
        return variables;
    }

    public boolean containsImp() {
        return found;
    }

    public void addVar(Var node) {
        variables.add(node.getName());
    }

    public void foundImp() {
        found = true;
    }
}
