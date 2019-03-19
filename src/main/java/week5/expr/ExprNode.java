package week5.expr;

public abstract class ExprNode {


    // Võimaldavad natuke mugavamalt luua neid objekte:
    public static ExprNode num(int i) {
        return new Num(i);
    }

    public static ExprNode neg(ExprNode e) {
        return new Neg(e);
    }

    public static ExprNode add(ExprNode e1, ExprNode e2) {
        return new Add(e1, e2);
    }

    public static ExprNode div(ExprNode e1, ExprNode e2) {
        return new Div(e1, e2);
    }

    // Visitor ja listener implementatsiooniks:
    public abstract <T> T accept(ExprVisitor<T> visitor);

    // Avaldise väärtustamiseks:
    public abstract int eval();

    public static void main(String[] args) {
        ExprNode expr = div(add(num(5), add(num(3), neg(num(2)))), num(2));
        System.out.println(expr.eval()); // tulemus: 3
    }

}
