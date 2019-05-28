package eksam1.ast;

import java.util.Arrays;
import java.util.List;

import static eksam1.ast.CondBinary.BinOp.*;
import static eksam1.ast.CondUnary.UnOp.CondNeg;
import static eksam1.ast.CondUnary.UnOp.CondNot;

public abstract class CondNode {



    // Programm ja muutujate deklaratsioonid
    public static CondNode prog(List<CondDecl> decls, CondNode expr) { return new CondProg(decls, expr); }
    public static List<CondDecl> decls(CondDecl... ds) { return Arrays.asList(ds); }
    public static CondDecl iv(String name) { return new CondDecl(name, true); }
    public static CondDecl bv(String name) { return new CondDecl(name, false); }

    // Literaalid ja muutuja kasutus
    public static CondNode il(int value) { return new CondLitInt(value); }
    public static CondNode bl(boolean value) { return new CondLitBool(value); }
    public static CondNode var(String name) { return new CondVar(name); }

    // Tingimus
    public static CondNode ifte(CondNode guard, CondNode trueExpr, CondNode falseExpr) {
        return new CondTernary(guard, trueExpr, falseExpr);
    }

    // Operatsioonid sümboli järgi
    public static CondNode binop(String op, CondNode left, CondNode right) {
        return new CondBinary(op, left, right);
    }
    public static CondNode unop(String op, CondNode expr) {
        return new CondUnary(op, expr);
    }

    // Võrdlus
    public static CondNode eq(CondNode left, CondNode right) { return new CondBinary(CondEq, left, right); }

    // Loogika
    public static CondNode not(CondNode expr) { return new CondUnary(CondNot, expr); }
    public static CondNode and(CondNode left, CondNode right) { return new CondBinary(CondAnd, left, right); }
    public static CondNode or(CondNode left, CondNode right) { return new CondBinary(CondOr, left, right); }

    // Aritmeetika
    public static CondNode neg(CondNode expr) { return new CondUnary(CondNeg, expr); }
    public static CondNode add(CondNode left, CondNode right) { return new CondBinary(CondAdd, left, right); }
    public static CondNode sub(CondNode left, CondNode right) { return new CondBinary(CondSub, left, right); }
    public static CondNode mul(CondNode left, CondNode right) { return new CondBinary(CondMul, left, right); }
    public static CondNode div(CondNode left, CondNode right) { return new CondBinary(CondDiv, left, right); }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CondNode)) return false;
        return toString().equals(obj.toString());
    }

    public abstract <T> T accept(CondAstVisitor<T> visitor);

    public static void main(String[] args) {
        CondNode test = prog(
                decls(iv("x"), bv("y")),
                add(var("x"), var("y"))
        );
        System.out.println(test);
    }
}
