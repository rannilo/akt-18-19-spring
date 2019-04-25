package week7;

import week6.TokenType;

import java.util.Map;

public abstract class ExprNode {

    public static ExprNode num(int v) {
        return new IntLiteral(v);
    }

    public static ExprNode var(String s) {
        return new Variable(s);
    }

    private static ExprNode binop(TokenType type, ExprNode l, ExprNode r) {
        return new BinOp(type, l, r);
    }

    public static ExprNode plus(ExprNode l, ExprNode r) {
        return binop(TokenType.PLUS, l, r);
    }

    public static ExprNode minus(ExprNode l, ExprNode r) {
        return binop(TokenType.MINUS, l, r);
    }

    public static ExprNode mul(ExprNode l, ExprNode r) {
        return binop(TokenType.TIMES, l, r);
    }

    public static ExprNode div(ExprNode l, ExprNode r) {
        return binop(TokenType.DIV, l, r);
    }

    public abstract int eval(Map<String, Integer> env);

    public String toPrettyString() {
        return toPrettyString(0);
    }

    protected abstract String toPrettyString(int contextPriority);

    public static class IntLiteral extends ExprNode {
        final private int value;

        public int getValue() {
            return value;
        }

        public IntLiteral(int value) {
            this.value = value;
        }

        @Override
        public int eval(Map<String, Integer> env) {
            return value;
        }


        @Override
        public String toString() {
            return "num(" + value + ")";
        }

        @Override
        protected String toPrettyString(int contextPriority) {
            return Integer.toString(value);
        }
    }

    public static class Variable extends ExprNode {

        private String name;

        public Variable(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int eval(Map<String, Integer> env) {
            return env.get(getName());
        }


        @Override
        public String toString() {
            return "var(\"" + name + "\")";
        }


        @Override
        protected String toPrettyString(int contextPriority) {
            return name;
        }
    }

    public static class BinOp extends ExprNode {

        private final TokenType op;

        private final ExprNode left;
        private final ExprNode right;

        public BinOp(TokenType token, ExprNode left, ExprNode right) {
            if (!isOperator(token)) throw new IllegalArgumentException();
            this.op = token;
            this.left = left;
            this.right = right;
        }

        private boolean isOperator(TokenType token) {
            switch (token) {
                case PLUS:
                case MINUS:
                case TIMES:
                case DIV:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int eval(Map<String, Integer> env) {
            int l = left.eval(env);
            int r = right.eval(env);
            switch (op) {
                case PLUS:
                    return l + r;
                case MINUS:
                    return l - r;
                case TIMES:
                    return l * r;
                case DIV:
                    return l / r;
                default:
                    throw new AssertionError("Impossible!");
            }
        }


        @Override
        public String toString() {
            return op.toString().toLowerCase() + "(" + left + ", " + right + ")";
        }

        @Override
        protected String toPrettyString(int contextPriority) {
            int prio = priorityOf(op);
            int assoc = prio + (isAssoc(op) ? 0 : 1);
            String content = left.toPrettyString(prio) + symbolOf(op) + right.toPrettyString(assoc);
            if (contextPriority > prio) return "(" + content + ")";
            else return content;
        }

        private boolean isAssoc(TokenType op) {
            switch (op) {
                case MINUS:
                case DIV:
                    return false;
                default:
                    return true;
            }
        }

        private String symbolOf(TokenType op) {
            switch (op) {
                case PLUS:
                    return "+";
                case MINUS:
                    return "-";
                case TIMES:
                    return "*";
                case DIV:
                    return "/";
                default:
                    return null;
            }
        }

        private int priorityOf(TokenType op) {
            switch (op) {
                case PLUS:
                case MINUS:
                    return 1;
                case TIMES:
                case DIV:
                    return 2;
            }
            return 0;
        }
    }
}
