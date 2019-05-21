package week11.eksamdemo.choice.choiceAst;

public class ChoiceValue extends ChoiceNode {

    private final int value;

    public ChoiceValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "val(" + value + ")";
    }


    @Override
    public <T> T accept(ChoiceVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
