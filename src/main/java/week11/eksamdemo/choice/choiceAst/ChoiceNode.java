package week11.eksamdemo.choice.choiceAst;

public abstract class ChoiceNode {

    // Võimaldavad natuke mugavamalt luua neid objekte:
    public static ChoiceNode val(int i) {
        return new ChoiceValue(i);
    }
    public static ChoiceNode add(ChoiceNode e1, ChoiceNode e2) {
        return new ChoiceAdd(e1, e2);
    }
    public static ChoiceNode choice(ChoiceNode trueChoice, ChoiceNode falseChoice) {
        return new ChoiceDecision(trueChoice, falseChoice);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChoiceNode)) return false;
        return toString().equals(obj.toString());
    }

    // Visitori implementatsiooniks:
    public abstract <T> T accept(ChoiceVisitor<T> visitor);

}
