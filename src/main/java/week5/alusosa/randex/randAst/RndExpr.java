package week5.alusosa.randex.randAst;

public abstract class RndExpr {


    // Võimaldavad natuke mugavamalt luua neid objekte:
    public static RndExpr num(int i) {
        return new RndNum(i);
    }

    public static RndExpr neg(RndExpr e) {
        return new RndNeg(e);
    }

    public static RndExpr add(RndExpr e1, RndExpr e2) {
        return new RndAdd(e1, e2);
    }

    public static RndExpr flip(RndExpr e1, RndExpr e2) {
        return new RndFlip(e1, e2);
    }

    // Visitor ja listener implementatsiooniks:
    public abstract <T> T accept(RndVisitor<T> visitor);

}
