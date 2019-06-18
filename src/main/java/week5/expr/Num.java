package week5.expr;

public class Num extends ExprNode {
    private final int num;

    public Num(int i) {
        this.num = i;
    }

    public int getValue() {
        return num;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int eval() {
        return num;
    }

}
