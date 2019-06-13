package eksam2.ast;

public class BologLit extends BologNode {
    private boolean value;

    public BologLit(boolean value) {
        this.value = value;
    }

    public boolean isTrue() {
        return value;
    }

    @Override
    public String toString() {
        return "tv(" + value + ")";
    }

    @Override
    public <T> T accept(BologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
