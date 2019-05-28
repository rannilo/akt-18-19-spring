package eksam1.ast;

public class CondLitBool extends CondNode {
    private boolean value;

    public CondLitBool(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "tv(" + value + ")";
    }

    @Override
    public <T> T accept(CondAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
