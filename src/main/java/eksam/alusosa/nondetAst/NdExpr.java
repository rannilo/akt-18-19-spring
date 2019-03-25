package eksam.alusosa.nondetAst;

public abstract class NdExpr {

    // Võimaldavad natuke mugavamalt luua neid objekte:
    public static NdExpr num(int i) {
        return new NdNum(i);
    }

    public static NdExpr inc(NdExpr e) {
        return new NdInc(e);
    }

    public static NdExpr mul(NdExpr e1, NdExpr e2) {
        return new NdMul(e1, e2);
    }

    public static NdExpr choice(String question, NdExpr trueChoice, NdExpr falseChoice) {
        return new NdChoice(question, trueChoice, falseChoice);
    }

    // Visitori implementatsiooniks:
    public abstract <T> T accept(NdVisitor<T> visitor);

}
