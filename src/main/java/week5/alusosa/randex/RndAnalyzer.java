package week5.alusosa.randex;

import week5.alusosa.randex.randAst.*;

import java.util.*;
import java.util.function.BooleanSupplier;

public class RndAnalyzer {

    // Tagastada kõik lehtede väärtused, mis asuvad mõne Flip vahetipu all.
    public static Set<Integer> findRandomLeafs(RndExpr expr) {
        Set<RndExpr> suurimadValikugaAlampuud = leiaSuurimadValikugaAlampuud(expr);
        RndVisitor<Set<Integer>> visitor = new RndVisitor<Set<Integer>>() {
            @Override
            protected Set<Integer> visit(RndNum num) {
                return Collections.singleton(num.getValue());
            }

            @Override
            protected Set<Integer> visit(RndNeg neg) {
                return visit(neg.getExpr());
            }

            @Override
            protected Set<Integer> visit(RndFlip flip) {
                return aggregate(visit(flip.getLeft()), visit(flip.getRight()));
            }

            @Override
            protected Set<Integer> visit(RndAdd add) {
                return aggregate(visit(add.getLeft()), visit(add.getRight()));
            }

            private Set<Integer> aggregate(Set<Integer> s1, Set<Integer> s2){
                Set<Integer> aggregate = new HashSet<>();
                aggregate.addAll(s1);
                aggregate.addAll(s2);
                return aggregate;
            }
        };
        Set<Integer> returnSet = new HashSet<>();
        for(RndExpr node: suurimadValikugaAlampuud){
            returnSet.addAll(node.accept(visitor));
        }
        return returnSet;
    }

    public static Set<RndExpr> leiaSuurimadValikugaAlampuud(RndExpr juur){
        RndVisitor<Set<RndExpr>> visitor = new RndVisitor<Set<RndExpr>>() {
            @Override
            protected Set<RndExpr> visit(RndNum num) {
                return new HashSet<>();
            }

            @Override
            protected Set<RndExpr> visit(RndNeg neg) {
                return new HashSet<>();
            }

            @Override
            protected Set<RndExpr> visit(RndFlip flip) {
                return Collections.singleton(flip);
            }

            @Override
            protected Set<RndExpr> visit(RndAdd add) {
                Set<RndExpr> aggregate = new HashSet<>();
                aggregate.addAll(visit(add.getLeft()));
                aggregate.addAll(visit(add.getRight()));
                return aggregate;
            }
        };
        return juur.accept(visitor);
    }

    // Väärtusta nüüd antud avaldist vasakult pareale, kasutades ettantud münti.
    public static int eval(RndExpr expr, BooleanSupplier coin) {
        RndVisitor<Integer> visitor = new RndVisitor<Integer>() {
            @Override
            protected Integer visit(RndNum num) {
                return num.getValue();
            }

            @Override
            protected Integer visit(RndNeg neg) {
                return (-1)*visit(neg.getExpr());
            }

            @Override
            protected Integer visit(RndFlip flip) {
                if(coin.getAsBoolean()) return visit(flip.getLeft());
                else return visit(flip.getRight());
            }

            @Override
            protected Integer visit(RndAdd add) {
                return visit(add.getLeft()) + visit(add.getRight());
            }
        };
        return expr.accept(visitor);
    }


    // Tagasta tõenäosus, et avaldise tulemus võrdub etteantud väärtusega.
    public static double prob(RndExpr expr, int value) {
        throw new UnsupportedOperationException();
    }
}

class Pair<X,Y>{
    private X x;
    private Y y;
    Pair(X x, Y y){
        this.x = x;
        this.y = y;
    }

    public X getKey() {
        return x;
    }

    public Y getValue() {
        return y;
    }
}