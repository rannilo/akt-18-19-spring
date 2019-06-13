package eksam2.ast;

public class BologNand extends BologNode {

    private BologNode leftExpr;
    private BologNode rightExpr;

    public BologNand(BologNode leftExpr, BologNode rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }


    public BologNode getLeftExpr() {
        return leftExpr;
    }

    public BologNode getRightExpr() {
        return rightExpr;
    }

    @Override
    public String toString() {
        return "nand(" + leftExpr + ", " + rightExpr + ")";
    }

    @Override
    public <T> T accept(BologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
