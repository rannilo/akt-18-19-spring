package eksam.alusosa.nondetAst;

public class NdNum extends NdExpr {
    private final int num;

    public NdNum(int i) {
        this.num = i;
    }

    public int getValue() {
        return num;
    }

    @Override
    public <T> T accept(NdVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
