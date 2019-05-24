package week11.eksamdemo.funlist.ast;

public class FunlistAdd extends FunlistNode {
    private final FunlistNode left;
    private final FunlistNode right;

    public FunlistAdd(FunlistNode left, FunlistNode right) {
        this.left = left;
        this.right = right;
    }

    public FunlistNode getLeft() {
        return left;
    }

    public FunlistNode getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "add(" + left + ", " + right + ")";
    }

    @Override
    public <T> T accept(FunlistAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
