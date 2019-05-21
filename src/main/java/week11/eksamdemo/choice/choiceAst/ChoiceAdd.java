package week11.eksamdemo.choice.choiceAst;

public class ChoiceAdd extends ChoiceNode {

    private final ChoiceNode left;
    private final ChoiceNode right;

    public ChoiceNode getLeft() {
        return left;
    }

    public ChoiceNode getRight() {
        return right;
    }

    public ChoiceAdd(ChoiceNode left, ChoiceNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "add(" + left + "," + right + ")";
    }

    @Override
    public <T> T accept(ChoiceVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
