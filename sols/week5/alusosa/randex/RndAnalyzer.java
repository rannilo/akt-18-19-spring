package week5.alusosa.randex;

import week5.alusosa.randex.randAst.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;

public class RndAnalyzer {

    public static Set<Integer> findRandomLeafs(RndExpr expr) {
        Set<Integer> result = new HashSet<>();
        new RndVisitor<Void>() {
            // peame loendama mitu flip oleme läbinud, sest muidu ei suuda eristada
            //    add(flip(..), num(5)) ei tohiks lisada 5.
            //    flip(flip(..), num(5)) aga peaks lisama 5.
            int flipDepth = 0;

            @Override
            protected Void visit(RndNum num) {
                if (flipDepth > 0) result.add(num.getValue());
                return null;
            }

            @Override
            protected Void visit(RndNeg neg) {
                return visit(neg.getExpr());
            }

            @Override
            protected Void visit(RndFlip flip) {
                flipDepth++;
                visit(flip.getLeft());
                visit(flip.getRight());
                flipDepth--;
                return null;
            }

            @Override
            protected Void visit(RndAdd add) {
                visit(add.getLeft());
                visit(add.getRight());
                return null;
            }
        }.visit(expr);
        return result;
    }

    // Väärtusta nüüd antud avaldist vasakult pareale, kasutades ettantud münti.
    // Kui coin.getAsBoolean() tagastab true -> minna vasakule.
    // Kui coin.getAsBoolean() tagastab false -> minna paremale.
    // Oluline on puu läbida ülalt-alla ja vasakult paremale, et mündi vised toimuks õigel ajal.
    // (See on see loomlik viis, kuidas meie eval meetodid on siiamaani ka olnud.)
    public static int eval(RndExpr expr, BooleanSupplier coin) {
        return new RndVisitor<Integer>() {
            @Override
            protected Integer visit(RndNum num) {
                return num.getValue();
            }

            @Override
            protected Integer visit(RndNeg neg) {
                return -visit(neg.getExpr());
            }

            @Override
            protected Integer visit(RndFlip flip) {
                return coin.getAsBoolean() ? visit(flip.getLeft()) : visit(flip.getRight());
            }

            @Override
            protected Integer visit(RndAdd add) {
                return visit(add.getLeft()) + visit(add.getRight());
            }
        }.visit(expr);
    }


    // Tagasta tõenäosus, et avaldise tulemus võrdub etteantud väärtusega.
    public static double prob(RndExpr expr, int value) {
        Map<Integer, Double> dist = new RndVisitor<Map<Integer, Double>>() {
            @Override
            protected Map<Integer, Double> visit(RndNum num) {
                HashMap<Integer, Double> map = new HashMap<>();
                map.put(num.getValue(), 1.0);
                return map;
            }

            @Override
            protected Map<Integer, Double> visit(RndNeg neg) {
                HashMap<Integer, Double> map = new HashMap<>();
                for (Map.Entry<Integer, Double> entry : visit(neg.getExpr()).entrySet()) {
                    map.put(entry.getKey(), -entry.getValue());
                }
                return map;
            }

            @Override
            protected Map<Integer, Double> visit(RndFlip flip) {
                HashMap<Integer, Double> map = new HashMap<>();
                for (Map.Entry<Integer, Double> entry : visit(flip.getLeft()).entrySet()) {
                    map.put(entry.getKey(), 0.5 * entry.getValue());
                }
                for (Map.Entry<Integer, Double> entry : visit(flip.getRight()).entrySet()) {
                    map.merge(entry.getKey(), 0.5 * entry.getValue(), (x, y) -> x + y);
                }
                return map;
            }

            @Override
            protected Map<Integer, Double> visit(RndAdd add) {
                HashMap<Integer, Double> map = new HashMap<>();
                for (Map.Entry<Integer, Double> entry1 : visit(add.getLeft()).entrySet()) {
                    for (Map.Entry<Integer, Double> entry2 : visit(add.getRight()).entrySet()) {
                        map.merge(entry1.getKey() + entry2.getKey(), entry1.getValue() * entry2.getValue(), (x, y) -> x + y);
                    }
                }
                return map;
            }
        }.visit(expr);

        return dist.getOrDefault(value, 0.0);
    }
}
