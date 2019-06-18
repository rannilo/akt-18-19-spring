package week5.expr;

public class Div extends ExprNode {
    private final ExprNode e1;
    private final ExprNode e2;

    public Div(ExprNode e1, ExprNode e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public ExprNode getNumerator() {
        return e1;
    }

    public ExprNode getDenominator() {
        return e2;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int eval() {
        return e1.eval() / e2.eval();
    }

}
