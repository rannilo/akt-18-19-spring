package eksam1.ast;

public class CondTernary extends CondNode {

    private CondNode guardExpr;
    private CondNode trueExpr;
    private CondNode falseExpr;

    public CondTernary(CondNode guardExpr, CondNode trueExpr, CondNode falseExpr) {
        this.guardExpr = guardExpr;
        this.trueExpr = trueExpr;
        this.falseExpr = falseExpr;
    }

    public CondNode getGuardExpr() {
        return guardExpr;
    }

    public CondNode getTrueExpr() {
        return trueExpr;
    }

    public CondNode getFalseExpr() {
        return falseExpr;
    }

    @Override
    public String toString() {
        return "ifte(" + guardExpr + ", " + trueExpr + ", " + falseExpr + ")";
    }

    @Override
    public <T> T accept(CondAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
