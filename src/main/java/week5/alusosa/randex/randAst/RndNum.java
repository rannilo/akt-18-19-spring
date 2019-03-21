package week5.alusosa.randex.randAst;

public class RndNum extends RndExpr {
    private final int num;

    public RndNum(int i) {
        this.num = i;
    }

    public int getValue() {
        return num;
    }

    @Override
    public <T> T accept(RndVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
