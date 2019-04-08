package week7;

import java.util.Map;

public abstract class ExprNode {

    public static ExprNode num(int v) {
        throw new UnsupportedOperationException();
    }

    public static ExprNode var(String s) {
        throw new UnsupportedOperationException();
    }

    public static ExprNode plus(ExprNode l, ExprNode r) {
        throw new UnsupportedOperationException();
    }

    public static ExprNode minus(ExprNode l, ExprNode r) {
        throw new UnsupportedOperationException();
    }

    public static ExprNode mul(ExprNode l, ExprNode r) {
        throw new UnsupportedOperationException();
    }

    public static ExprNode div(ExprNode l, ExprNode r) {
        throw new UnsupportedOperationException();
    }

    public abstract int eval(Map<String, Integer> env);

    public String toPrettyString() {
        return toString();
    }
}
