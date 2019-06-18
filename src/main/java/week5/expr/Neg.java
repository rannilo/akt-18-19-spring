package week5.expr;

public class Neg extends ExprNode {

    private final ExprNode expr;

    public Neg(ExprNode e) {
        this.expr = e;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int eval() {
        return expr.eval();
    }


}
