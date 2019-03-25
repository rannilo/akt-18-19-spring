package eksam.alusosa.nondetAst;

public class NdInc extends NdExpr {

    private final NdExpr expr;

    public NdInc(NdExpr e) {
        this.expr = e;
    }

    public NdExpr getExpr() {
        return expr;
    }

    @Override
    public <T> T accept(NdVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
