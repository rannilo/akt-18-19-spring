package eksam.alusosa.nondetAst;

public class NdMul extends NdExpr {
    private final NdExpr e1;
    private final NdExpr e2;

    public NdMul(NdExpr e1, NdExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public NdExpr getLeft() {
        return e1;
    }

    public NdExpr getRight() {
        return e2;
    }

    @Override
    public <T> T accept(NdVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
