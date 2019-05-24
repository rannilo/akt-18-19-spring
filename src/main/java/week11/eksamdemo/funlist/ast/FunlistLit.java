package week11.eksamdemo.funlist.ast;

public class FunlistLit extends FunlistNode {
    private final int n;

    public FunlistLit(int n) {
        this.n = n;
    }

    public int getVal() {
        return n;
    }

    @Override
    public String toString() {
        return "lit(" + n + ")";
    }

    @Override
    public <T> T accept(FunlistAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
