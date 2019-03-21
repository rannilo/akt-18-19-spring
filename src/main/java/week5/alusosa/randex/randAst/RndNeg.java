package week5.alusosa.randex.randAst;

public class RndNeg extends RndExpr {

    private final RndExpr expr;

    public RndNeg(RndExpr e) {
        this.expr = e;
    }

    public RndExpr getExpr() {
        return expr;
    }

    @Override
    public <T> T accept(RndVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
