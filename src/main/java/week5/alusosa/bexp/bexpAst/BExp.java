package week5.alusosa.bexp.bexpAst;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public abstract class BExp {
    // Võimaldavad natuke mugavamalt luua neid objekte:
    public static BExp var(char b) {
        return new Var(b);
    }

    public static BExp imp(BExp e1, BExp e2) {
        return new Imp(e1, e2);
    }

    public static BExp or(BExp e1, BExp e2) {
        return new Or(e1, e2);
    }

    public static BExp not(BExp e) {
        return new Not(e);
    }

    private final List<BExp> children;

    public BExp(BExp... children) {
        this.children = Arrays.asList(children);
    }

    public List<BExp> getChildren() {
        return children;
    }

    public BExp getChild(int i) {
        return children.get(i);
    }


    // Visitor ja listener implementatsiooniks:
    public abstract <T> T accept(BExpVisitor<T> visitor);

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", getClass().getSimpleName().toLowerCase() + "(", ")");
        for (BExp child : children) joiner.add(child.toString());
        return joiner.toString();
    }

    public static void main(String[] args) {
        System.out.println(not(or(var('A'), var('B'))));
    }

}
