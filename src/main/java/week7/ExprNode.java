package week7;

import week6.TokenType;

import java.util.Map;

import static week6.TokenType.*;

public abstract class ExprNode {

    public static ExprNode num(int v) {
        return new ExprNum(v);
    }

    public static ExprNode var(String s) {
        return new ExprVar(s);
    }

    public static ExprNode plus(ExprNode l, ExprNode r) {
        return new ExprPlus(l, r);
    }

    public static ExprNode minus(ExprNode l, ExprNode r) {
        return new ExprMinus(l, r);
    }

    public static ExprNode mul(ExprNode l, ExprNode r) {
        return new ExprMul(l, r);
    }

    public static ExprNode div(ExprNode l, ExprNode r) {
        return new ExprDiv(l, r);
    }

    public abstract int eval(Map<String, Integer> env);

    public abstract String toPrettyString();

    private static class ExprNum extends ExprNode {
        private int val;

        public ExprNum(int val) {
            this.val = val;
        }

        @Override
        public int eval(Map<String, Integer> env) {
            return val;
        }

        @Override
        public String toPrettyString() {
            return Integer.toString(val);
        }
    }

    private static class ExprVar extends ExprNode {
        private String name;

        public ExprVar(String name) {
            this.name = name;
        }

        @Override
        public int eval(Map<String, Integer> env) {
            return env.get(name);
        }

        @Override
        public String toPrettyString() {
            return name;
        }
    }

    private static class ExprPlus extends ExprNode {
        private ExprNode l;
        private ExprNode r;

        public ExprPlus(ExprNode l, ExprNode r) {
            this.l = l;
            this.r = r;
        }

        @Override
        public int eval(Map<String, Integer> env) {
            return l.eval(env) + r.eval(env);
        }

        @Override
        public String toPrettyString() {
            return l.toPrettyString() + "+" + r.toPrettyString();
        }
    }

    private static class ExprMinus extends ExprNode {
        private ExprNode l;
        private ExprNode r;

        public ExprMinus(ExprNode l, ExprNode r) {
            this.l = l;
            this.r = r;
        }

        @Override
        public int eval(Map<String, Integer> env) {
            return l.eval(env) - r.eval(env);
        }

        @Override
        public String toPrettyString() {
            String leftString = l.toPrettyString();
            String rightString = r.toPrettyString();
            if (priorityOf(l) < priorityOf(this)) leftString = "(" + leftString + ")";
            if (priorityOf(r) <= priorityOf(this)) rightString = "(" + rightString + ")";
            return leftString + "-" + rightString;
        }
    }

    private static class ExprMul extends ExprNode {
        private ExprNode l;
        private ExprNode r;

        public ExprMul(ExprNode l, ExprNode r) {
            this.l = l;
            this.r = r;
        }

        @Override
        public int eval(Map<String, Integer> env) {
            return l.eval(env) * r.eval(env);
        }

        @Override
        public String toPrettyString() {
            String leftString = l.toPrettyString();
            String rightString = r.toPrettyString();
            if (priorityOf(l) < priorityOf(this)) leftString = "(" + leftString + ")";
            if (priorityOf(r) < priorityOf(this)) rightString = "(" + rightString + ")";
            return leftString + "*" + rightString;
        }
    }

    private static class ExprDiv extends ExprNode {
        private ExprNode l;
        private ExprNode r;

        public ExprDiv(ExprNode l, ExprNode r) {
            this.l = l;
            this.r = r;
        }

        @Override
        public int eval(Map<String, Integer> env) {
            return l.eval(env) / r.eval(env);
        }


        @Override
        public String toPrettyString() {
            String leftString = l.toPrettyString();
            String rightString = r.toPrettyString();
            if (priorityOf(l) < priorityOf(this)) leftString = "(" + leftString + ")";
            if (priorityOf(r) <= priorityOf(this)) rightString = "(" + rightString + ")";
            return leftString + "/" + rightString;
        }
    }

    private static int priorityOf(ExprNode exprNode){
        if(exprNode instanceof ExprMinus) return 1;
        if(exprNode instanceof ExprPlus) return 1;
        if(exprNode instanceof ExprMul) return 2;
        if(exprNode instanceof ExprDiv) return 2;
        return 3;
    }
}

