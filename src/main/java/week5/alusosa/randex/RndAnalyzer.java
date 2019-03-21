package week5.alusosa.randex;

import week5.alusosa.randex.randAst.RndExpr;

import java.util.Set;
import java.util.function.BooleanSupplier;

public class RndAnalyzer {

    // Tagastada kõik lehtede väärtused, mis asuvad mõne Flip vahetipu all.
    public static Set<Integer> findRandomLeafs(RndExpr expr) {
        throw new UnsupportedOperationException();
    }

    // Väärtusta nüüd antud avaldist vasakult pareale, kasutades ettantud münti.
    public static int eval(RndExpr expr, BooleanSupplier coin) {
        throw new UnsupportedOperationException();
    }


    // Tagasta tõenäosus, et avaldise tulemus võrdub etteantud väärtusega.
    public static double prob(RndExpr expr, int value) {
        throw new UnsupportedOperationException();
    }
}
