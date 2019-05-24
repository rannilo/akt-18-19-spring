package week11.eksamdemo;

import cma.CMaInterpreter;
import cma.CMaStack;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week11.eksamdemo.funlist.FunlistAlusosa;
import week11.eksamdemo.funlist.FunlistLoviosa.IntVarargOperator;
import week11.eksamdemo.funlist.ast.FunlistNode;
import week11.eksamdemo.funlist.ast.FunlistProg;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static week11.eksamdemo.funlist.FunlistAlusosa.allLiterals;
import static week11.eksamdemo.funlist.FunlistLoviosa.codeGen;
import static week11.eksamdemo.funlist.FunlistLoviosa.toJavaFun;
import static week11.eksamdemo.funlist.FunlistPohiosa.makeFlistAst;
import static week11.eksamdemo.funlist.ast.FunlistNode.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FunlistTest {

    private Map<String, FunlistProg> solmap = new HashMap<>();

    @Before
    public void setUp() {

        solmap.put("kala() = 100",
                prog(fun("kala", lit(100))));

        solmap.put("koer(x, y) = x + y + 100",
                prog(fun("koer", add(add(var('x'), var('y')), lit(100)), 'x', 'y')));

        solmap.put("kala() = 100\n koer() = 200",
                prog(fun("kala", lit(100)),
                        fun("koer", lit(200))));

        solmap.put("fun(x) = 10\nid(y) = y",
                prog(fun("fun", lit(10), 'x'),
                        fun("id", var('y'), 'y')));

        solmap.put("sum(x,y) = x + y",
                prog(fun("sum", add(var('x'), var('y')), 'x', 'y')));

        solmap.put("sum(x,y) = x + z",
                prog(fun("sum", add(var('x'), var('z')), 'x', 'y')));

        solmap.put("inc(x) = x +1",
                prog(fun("inc", add(var('x'), lit(1)), 'x')));

        solmap.put("sum(x,y) = x + y\ninc(x) = x +1\nf() = 100",
                prog(fun("sum", add(var('x'), var('y')), 'x', 'y'),
                        fun("inc", add(var('x'), lit(1)), 'x'),
                        fun("f", lit(100))
                ));

        solmap.put("sum(x,y) = z + 1\n inc(x) = x + 1 \n foo(y) = x + 1",
                prog(fun("sum", add(var('z'), lit(1)), 'x', 'y'),
                        fun("inc", add(var('x'), lit(1)), 'x'),
                        fun("foo", add(var('x'), lit(1)), 'y')
                ));
    }

    private FunlistNode ast(String input) {
        // Kui usaldad oma ASTi tegija, siis asenda selle meetodi keha järgmise reaga
        // return makeFlistAst(input);
        if (!solmap.containsKey(input)) {
            throw new NoSuchElementException("See on fake parser siin. Ei saa sisendeid niisama muuta!");
        }
        return solmap.get(input);
    }

    @SafeVarargs
    private static <T> Set<T> setOf(T... elems) {
        return new HashSet<>(asList(elems));
    }

    @Test
    public void test01_alusosa_literals() {
        assertEquals(setOf(100), allLiterals(ast("kala() = 100")));
        assertEquals(setOf(100, 200), allLiterals(ast("kala() = 100\n koer() = 200")));
        assertEquals(setOf(1), allLiterals(ast("inc(x) = x +1")));
        assertEquals(setOf(), allLiterals(ast("sum(x,y) = x + y")));
        assertEquals(setOf(1, 100), allLiterals(ast("sum(x,y) = x + y\ninc(x) = x +1\nf() = 100")));
    }

    @Test
    public void test02_alusosa_constFun() {
        assertEquals(setOf("kala"), FunlistAlusosa.constFuns(ast("kala() = 100")));
        assertEquals(setOf("fun"), FunlistAlusosa.constFuns(ast("fun(x) = 10\nid(y) = y")));
        assertEquals(setOf("f"), FunlistAlusosa.constFuns(ast("sum(x,y) = x + y\ninc(x) = x +1\nf() = 100")));
        assertEquals(setOf("sum", "foo"), FunlistAlusosa.constFuns(ast("sum(x,y) = z + 1\n inc(x) = x + 1 \n foo(y) = x + 1")));
    }


    private <K,V> Map<K,V> zipToMap(List<K> keys, List<V> values) {
        return IntStream.range(0, keys.size()).boxed().collect(toMap(keys::get, values::get));
    }

    @Test
    public void test03_alusosa_evalA() {
        Map<Character, Integer> env1 = zipToMap(asList('x', 'y', 'z'), asList(23, 44, 75));
        Map<Character, Integer> env2 = zipToMap(asList('x', 'y', 'z'), asList(36, 56, 200));

        checkEval(100, "kala() = 100", env1);

        checkEval(24, "inc(x) = x +1", env1);
        checkEval(37, "inc(x) = x +1", env2);

        checkEval(167, "koer(x, y) = x + y + 100", env1);
        checkEval(192, "koer(x, y) = x + y + 100", env2);
    }

    private void checkEval(int expected, String input, Map<Character, Integer> env) {
        int actual = FunlistAlusosa.eval(ast(input), env);
        assertEquals(expected, actual);
    }

    @Test
    public void test04_alusosa_evalB() {
        Map<Character, Integer> env1 = zipToMap(asList('x', 'y', 'z'), asList(23, 44, 75));
        Map<Character, Integer> env2 = zipToMap(asList('x', 'y', 'z'), asList(36, 56, 200));

        checkEval(100, "kala() = 100", env1, "kala");

        checkEval(78, "inc(x) = x +1", env1, "inc", 77);
        checkEval(8, "sum(x,y) = x + y", env1, "sum", 3, 5);
        checkEval(112, "koer(x, y) = x + y + 100", env1, "koer", 3, 9);

        checkEval(85, "sum(x,y) = x + z", env1, "sum", 10, 20);
        checkEval(210, "sum(x,y) = x + z", env2, "sum", 10, 20);

        checkEval(11, "sum(x,y) = z + 1\n inc(x) = x + 1 \n foo(y) = x + 1", env2, "inc", 10);
        checkEval(24, "sum(x,y) = z + 1\n inc(x) = x + 1 \n foo(y) = x + 1", env1, "foo", 10);
        checkEval(37, "sum(x,y) = z + 1\n inc(x) = x + 1 \n foo(y) = x + 1", env2, "foo", 10);
    }

    private void checkEval(int expected, String input, Map<Character, Integer> globs, String f, Integer... args) {
        int actual = FunlistAlusosa.eval(ast(input), globs, f, args);
        assertEquals(expected, actual);
    }


    @Test
    public void test05_pohiosa_grammar() {
        ok("kala() = 100");
        ok("koer(x, y) = x + y + 100");
        ok("fun(x) = 10\nid(y) = y");
        no("100");
        no("f = x + x");

        ok("kala(x) = 10");
        no("kala(pikk) = 10");
        ok("f(x) = 10");
        no("f(xx) = 10");
    }

    private static void ok(String input) {
        makeFlistAst(input);
    }

    private static void no(String input) {
        try {
            makeFlistAst(input);
        } catch (ParseCancellationException e)  {
            return;
        }
        fail("Vigane sisend!");
    }

    @Test
    public void test06_pohiosa_ast() {
        for (Map.Entry<String, FunlistProg> progEntry : solmap.entrySet()) {
            String input = progEntry.getKey();
            FunlistProg prog = progEntry.getValue();
            assertEquals(prog, makeFlistAst(input));
        }
    }

    @Test
    public void test07_loviosa_codegen() {
        checkCodegen(100, "kala() = 100", "kala" );
        checkCodegen(100, "kala() = 100\n koer() = 200", "kala" );
        checkCodegen(200, "kala() = 100\n koer() = 200", "koer" );

        checkCodegen(78, "inc(x) = x +1", "inc", 77);
        checkCodegen(25, "sum(x,y) = x + y", "sum", 8, 17);
    }

    private void checkCodegen(int expected, String input, String f, Integer... stack) {
        CMaStack initialStack = new CMaStack(asList(stack));
        assertEquals(expected, CMaInterpreter.run(codeGen(ast(input)).get(f), initialStack).peek());
    }


    @Test
    public void test08_loviosa_toJava() {
        IntVarargOperator kala = toJavaFun(ast("kala() = 100"));
        assertEquals(100, kala.apply());

        IntVarargOperator sum = toJavaFun((ast("sum(x,y) = x + y")));
        assertEquals(14, sum.apply(4, 10));
        assertEquals(169, sum.apply(77, 92));

        IntVarargOperator inc = toJavaFun((ast("inc(x) = x +1")));
        assertEquals(5, inc.apply(4));
    }


}