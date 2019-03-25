package eksam.alusosa;

import eksam.alusosa.nondetAst.NdExpr;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.function.Predicate;

import static eksam.alusosa.nondetAst.NdExpr.*;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NondetAnalyzerTest {

    private static String q1 = "Are you happy?";
    private static String q2 = "Are you sad?";
    private static String q3 = "What is the meaning of life?";

    private Random rnd = new Random();
    private Predicate<String> oracle1 = s -> s.equals(q1) || s.equals(q3);
    private Predicate<String> oracle2 = oracle1.negate();
    private Predicate<String> oracle3 = s -> rnd.nextBoolean();

    private NdExpr ex0 = inc(num(2));
    private NdExpr ex1 = mul(num(3), num(5));
    private NdExpr ex2 = choice(q1, num(2), num(4));
    private NdExpr ex3 = choice(q2, ex1, ex2);
    private NdExpr ex4 = choice(q3, ex1, inc(choice(q2, num(14), num(14))));
    private NdExpr ex5 = mul(num(0), ex2);
    private NdExpr ex6 = mul(ex2, choice(q1, num(0), inc(num(-1))));



    @Test
    public void test01_getAllLeafsNotUnder_simple() {
        assertEquals(new HashSet<>(Collections.singletonList(2)), NondetAnalyzer.getAllLeafsNotUnder(ex0, q1));
        assertEquals(new HashSet<>(Arrays.asList(3, 5)), NondetAnalyzer.getAllLeafsNotUnder(ex1, q1));
    }

    @Test
    public void test02_getAllLeafsNotUnder() {
        assertEquals(new HashSet<>(Collections.emptyList()), NondetAnalyzer.getAllLeafsNotUnder(ex2, q1));
        assertEquals(new HashSet<>(Arrays.asList(3, 5)), NondetAnalyzer.getAllLeafsNotUnder(ex3, q1));
        assertEquals(new HashSet<>(Arrays.asList(3, 5, 14)), NondetAnalyzer.getAllLeafsNotUnder(ex4, q1));
    }

    @Test
    public void test03_eval_simple() {
        assertEquals(3, NondetAnalyzer.eval(ex0, oracle1));
        assertEquals(15, NondetAnalyzer.eval(ex1, oracle1));
    }

    @Test
    public void test04_eval() {
        assertEquals(2, NondetAnalyzer.eval(ex2, oracle1));
        assertEquals(4, NondetAnalyzer.eval(ex2, oracle2));
        assertEquals(15, NondetAnalyzer.eval(ex3, oracle2));
        assertEquals(2, NondetAnalyzer.eval(ex3, oracle1));
        assertEquals(15, NondetAnalyzer.eval(ex4, oracle3));
    }

    @Test
    public void test05_isDeterministic_simple() {
        assertTrue(NondetAnalyzer.isDeterministic(ex0));
        assertTrue(NondetAnalyzer.isDeterministic(ex1));
        assertFalse(NondetAnalyzer.isDeterministic(ex2));
        assertFalse(NondetAnalyzer.isDeterministic(ex3));
    }

    @Test
    public void test06_isDeterministic() {
        assertTrue(NondetAnalyzer.isDeterministic(ex4));
        assertTrue(NondetAnalyzer.isDeterministic(ex5));
        assertTrue(NondetAnalyzer.isDeterministic(ex6));
    }
}
