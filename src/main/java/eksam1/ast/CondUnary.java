package eksam1.ast;

import cma.instruction.CMaBasicInstruction;

import java.util.Arrays;
import java.util.Map;
import java.util.function.UnaryOperator;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class CondUnary extends CondNode {

    public enum UnOp {
        CondNeg("-", x -> -(int)x),
        CondNot("!", x -> !(boolean)x);

        private String symb;
        private UnaryOperator<Object> javaOp;

        UnOp(String symb, UnaryOperator<Object> javaOp) {
            this.symb = symb;
            this.javaOp = javaOp;
        }

        public String getSymb() {
            return symb;
        }

        public UnaryOperator<Object> toJava() {
            return javaOp;
        }

        public CMaBasicInstruction.Code toCMa() {
            return CMaBasicInstruction.Code.valueOf(toString().substring(4).toUpperCase());
        }

        private final static Map<String, UnOp> symbolMap =
                Arrays.stream(UnOp.values()).collect(toMap(UnOp::getSymb, identity()));

        public static UnOp fromSymb(String symb) {
            return symbolMap.get(symb);
        }

    }

    private UnOp op;
    private CondNode expr;
    private CondNode rightExpr;

    public CondUnary(UnOp op, CondNode expr) {
        this.op = op;
        this.expr = expr;
    }

    public CondUnary(String symb, CondNode expr) {
        this(UnOp.fromSymb(symb), expr);
    }

    public UnOp getOp() {
        return op;
    }

    public CondNode getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return getOpName() + "(" + expr + ")";
    }

    private String getOpName() {
        return op.toString().substring(4).toLowerCase();
    }

    @Override
    public <T> T accept(CondAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
