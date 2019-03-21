package week5.alusosa;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week5.alusosa.randex.RndAnalyzer;
import week5.alusosa.randex.randAst.RndExpr;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.BooleanSupplier;

import static org.junit.Assert.assertEquals;
import static week5.alusosa.randex.randAst.RndExpr.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RndAnalyzerTest {

    private RndExpr ex1 = add(num(4),num(6));
    private RndExpr ex2 = flip(num(4),num(6));
    private RndExpr ex3 = flip(flip(num(4), num(5)), num(6));
    private RndExpr ex4 = add(flip(num(4), num(5)), num(6));
    private RndExpr ex5 = flip(num(4), flip(num(7), num(66)));
    private RndExpr ex6 = flip(add(num(4), num(5)), num(8));
    private RndExpr ex7 = add(num(3), flip(num(33), num(9)));
    private RndExpr ex8 = flip(ex5, ex6);
    private RndExpr ex9 = add(ex2, ex2);

    private BooleanSupplier falseCoin = () -> false;
    private BooleanSupplier trueCoin = () -> true;

    private BooleanSupplier altCoin(boolean start) {
        return new BooleanSupplier() {
            boolean state = !start;

            @Override
            public boolean getAsBoolean() {
                state = !state;
                return state;
            }
        };
    }

    @Test
    public void test01_leafs() {
        assertEquals(new HashSet<>(), RndAnalyzer.findRandomLeafs(ex1));
        assertEquals(new HashSet<>(Arrays.asList(4,6)), RndAnalyzer.findRandomLeafs(ex2));
        assertEquals(new HashSet<>(Arrays.asList(4,5,6)), RndAnalyzer.findRandomLeafs(ex3));
        assertEquals(new HashSet<>(Arrays.asList(4,5)), RndAnalyzer.findRandomLeafs(ex4));
        assertEquals(new HashSet<>(Arrays.asList(4,66,7)), RndAnalyzer.findRandomLeafs(ex5));
        assertEquals(new HashSet<>(Arrays.asList(4,5,8)), RndAnalyzer.findRandomLeafs(ex6));
        assertEquals(new HashSet<>(Arrays.asList(33,9)), RndAnalyzer.findRandomLeafs(ex7));
    }

    @Test
    public void test02_eval() {
        assertEquals(10, RndAnalyzer.eval(ex1, trueCoin));

        assertEquals(4, RndAnalyzer.eval(ex2, trueCoin));
        assertEquals(6, RndAnalyzer.eval(ex2, falseCoin));

        assertEquals(4, RndAnalyzer.eval(ex5, trueCoin));
        assertEquals(66, RndAnalyzer.eval(ex5, falseCoin));
        assertEquals(4, RndAnalyzer.eval(ex5, altCoin(true)));
        assertEquals(7, RndAnalyzer.eval(ex5, altCoin(false)));

        assertEquals(4, RndAnalyzer.eval(ex8, trueCoin));
        assertEquals(8, RndAnalyzer.eval(ex8, falseCoin));
        assertEquals(7, RndAnalyzer.eval(ex8, altCoin(true)));
        assertEquals(9, RndAnalyzer.eval(ex8, altCoin(false)));
    }

    @Test
    public void test03_prob() {
        assertEquals(1.0, RndAnalyzer.prob(ex1, 10), 0.0);
        assertEquals(0.0, RndAnalyzer.prob(ex1, 40), 0.0);

        assertEquals(0.5, RndAnalyzer.prob(ex2, 4), 0.0);
        assertEquals(0.5, RndAnalyzer.prob(ex2, 6), 0.0);
        assertEquals(0.0, RndAnalyzer.prob(ex2, 10), 0.0);

        assertEquals(0.25, RndAnalyzer.prob(ex3, 4), 0.0);
        assertEquals(0.25, RndAnalyzer.prob(ex3, 5), 0.0);
        assertEquals(0.50, RndAnalyzer.prob(ex3, 6), 0.0);

        // See on see oluline asi, et (0|1)+(0|1) puhul on summa 1 tõenäolisem.
        assertEquals(0.25, RndAnalyzer.prob(ex9, 8), 0.0);
        assertEquals(0.25, RndAnalyzer.prob(ex9, 12), 0.0);
        assertEquals(0.50, RndAnalyzer.prob(ex9, 10), 0.0);

        assertEquals(0.25, RndAnalyzer.prob(ex8, 8), 0.0);
        assertEquals(0.125, RndAnalyzer.prob(ex8, 7), 0.0);

    }
}