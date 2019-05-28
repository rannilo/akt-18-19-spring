package eksam1.ast;

public class CondLitInt extends CondNode {
    private int value;

    public CondLitInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "num(" + value + ")";
    }

    @Override
    public <T> T accept(CondAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
