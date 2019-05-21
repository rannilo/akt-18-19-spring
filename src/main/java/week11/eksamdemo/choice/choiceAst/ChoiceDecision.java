package week11.eksamdemo.choice.choiceAst;

public class ChoiceDecision extends ChoiceNode {

    private final ChoiceNode trueChoice;
    private final ChoiceNode falseChoice;

    public ChoiceNode getTrueChoice() {
        return trueChoice;
    }

    public ChoiceNode getFalseChoice() {
        return falseChoice;
    }

    public ChoiceDecision(ChoiceNode trueChoice, ChoiceNode falseChoice) {
        this.trueChoice = trueChoice;
        this.falseChoice = falseChoice;
    }

    @Override
    public String toString() {
        return "choice(" + trueChoice + "," + falseChoice + ")";
    }

    @Override
    public <T> T accept(ChoiceVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
