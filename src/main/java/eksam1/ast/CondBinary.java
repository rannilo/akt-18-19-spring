package eksam1.ast;

import cma.instruction.CMaBasicInstruction;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BinaryOperator;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class CondBinary extends CondNode {

    public enum BinOp {
        CondEq("on", Object::equals),

        CondAnd("&", (x,y) -> (boolean)x && (boolean)y),
        CondOr("|", (x,y) -> (boolean)x || (boolean)y),

        CondAdd("+", (x,y) -> (int)x + (int)y),
        CondSub("-", (x,y) -> (int)x - (int)y),
        CondMul("*", (x,y) -> (int)x * (int)y),
        CondDiv("/", (x,y) -> (int)x / (int)y);

        private String symb;
        private BinaryOperator<Object> javaOp;

        BinOp(String symb, BinaryOperator<Object> javaOp) {
            this.symb = symb;
            this.javaOp = javaOp;
        }

        public String getSymb() {
            return symb;
        }

        public BinaryOperator<Object> toJava() {
            return javaOp;
        }

        public CMaBasicInstruction.Code toCMa() {
            return CMaBasicInstruction.Code.valueOf(toString().substring(4).toUpperCase());
        }

        private final static Map<String, BinOp> symbolMap =
                Arrays.stream(BinOp.values()).collect(toMap(BinOp::getSymb, identity()));

        public static BinOp fromSymb(String symb) {
            return symbolMap.get(symb);
        }

    }

    private BinOp op;
    private CondNode leftExpr;
    private CondNode rightExpr;

    public CondBinary(BinOp op, CondNode leftExpr, CondNode rightExpr) {
        this.op = op;
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public CondBinary(String symb, CondNode leftExpr, CondNode rightExpr) {
        this(BinOp.fromSymb(symb), leftExpr, rightExpr);
    }

    public BinOp getOp() {
        return op;
    }


    public CondNode getLeftExpr() {
        return leftExpr;
    }

    public CondNode getRightExpr() {
        return rightExpr;
    }

    @Override
    public String toString() {
        return getOpName() + "(" + leftExpr + ", " + rightExpr + ")";
    }

    private String getOpName() {
        return op.toString().substring(4).toLowerCase();
    }

    @Override
    public <T> T accept(CondAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
