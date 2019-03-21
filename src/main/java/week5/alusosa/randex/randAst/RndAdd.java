package week5.alusosa.randex.randAst;

public class RndAdd extends RndExpr {
    private final RndExpr e1;
    private final RndExpr e2;

    public RndAdd(RndExpr e1, RndExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public RndExpr getLeft() {
        return e1;
    }

    public RndExpr getRight() {
        return e2;
    }

    @Override
    public <T> T accept(RndVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
