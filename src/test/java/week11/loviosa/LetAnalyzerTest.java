package week11.loviosa;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week9.pohiosa.letex.letAst.LetAvaldis;

import static org.junit.Assert.*;
import static week11.loviosa.LetAnalyzer.onVaja;
import static week11.loviosa.LetAnalyzer.optimeeri;
import static week9.pohiosa.letex.letAst.LetAvaldis.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LetAnalyzerTest {


    @Test
    public void test01_sem1() {
        assertTrue(onVaja("x", var("x")));
        assertFalse(onVaja("x", num(0)));
        assertFalse(onVaja("x", var("y")));
        assertTrue(onVaja("kala", vahe(var("x"), var("kala"))));
        assertTrue(onVaja("kala", vahe(var("kala"), var("y"))));
        assertFalse(onVaja("kala", vahe(var("x"), var("y"))));
    }

    @Test
    public void test02_sem2() {
        assertTrue(onVaja("x", let("y", num(10), var("x"))));
        assertTrue(onVaja("x", let("y", var("x"), var("y"))));
        assertFalse(onVaja("z", let("y", num(10), var("x"))));
        assertFalse(onVaja("z", let("y", var("x"), var("y"))));
    }


    @Test
    public void test03_sem3() {
        assertTrue(onVaja("x", let("y", num(10), var("x"))));
        assertTrue(onVaja("x", let("y", var("x"), var("y"))));
        assertTrue(onVaja("x", let("y",vahe(var("x"),num(1)),var("y"))));
        assertFalse(onVaja("z", let("y", num(10), var("x"))));
        assertFalse(onVaja("y", let("y", var("x"), var("y"))));
        //assertTrue(onVaja("z", let("x",num(5),let("y",var("z"),let("z",var("x"),var("z"))))));
        //assertTrue(onVaja("y", let("x",num(5),let("y",var("y"),let("z",var("x"),var("z"))))));
        assertFalse(onVaja("z", let("x",num(5),let("y",var("y"),let("z",var("x"),var("z"))))));

        //assertFalse(onVaja("x", let("y", var("x"), num(5)))); // ülesandes spetsifitseerimata juht
    }

    @Test
    public void test04_opt1() {
        assertEquals(var("y"), optimeeri(let("x", num(5), var("y"))));
        assertEquals(let("x", num(5), var("x")), optimeeri(let("x", num(5), var("x"))));
    }

    @Test
    public void test05_opt2() {
        assertEquals(let("x",num(5),let("z",var("x"),var("z"))),
                optimeeri(let("x",num(5),let("y",num(10),let("z",var("x"),var("z"))))));
        assertEquals(let("a", var("y"), var("a")),
                optimeeri(let("a", let("x", num(5), var("y")), var("a"))));
    }

    @Test
    public void test06_opt3() {
        assertEquals(var("z"), optimeeri(let("x",num(10),let("y",var("x"),var("z")))));
        assertEquals(var("y"), optimeeri(let("a", let("a", num(10), var("a")), var("y"))));
        assertEquals(let("a", var("a"), vahe(var("b"), var("a"))),
                optimeeri(let("a", let("b", var("x"), var("a")), vahe(var("b"), var("a")))));
    }


    @Test
    public void test07_eval_literaalVahe() {
        checkEval(num(0), 0);
        checkEval(num(97519), 97519);
        checkEval(vahe(num(10),num(5)), 5);
        checkEval(vahe(vahe(num(10),num(5)),num(9)), -4);
        checkEval(vahe(vahe(num(10),num(5)),vahe(num(9),num(22))), 18);
    }

    @Test
    public void test08_eval_let() {
        checkEval(let("x",num(5),var("x")), 5);
        checkEval(let("x",num(5),vahe(var("x"),num(10))), -5);
        checkEval(let("x",vahe(num(10),num(3)),vahe(var("x"),num(100))), -93);
        checkEval(let("x",num(100),num(5)), 5);
        checkEval(vahe(num(55),let("x",num(100),num(5))), 50);
        checkEval(var("a"), null);
        checkEval(let("x",num(100),vahe(var("z"),var("x"))), null);
    }

    @Test
    public void test09_eval_sum() {
        checkEval(sum("i",num(1),num(4),var("i")), 10);
        checkEval(sum("i",num(1),num(4),num(1)), 4);
        checkEval(sum("i",num(1),num(40),vahe(var("i"),num(10))), 420);
        checkEval(sum("x",vahe(num(1),num(1)),vahe(vahe(num(10),num(1)),num(1)),var("i")), null);
        checkEval(sum("x",num(1),num(4),var("y")), null);
    }

    @Test
    public void test10_eval_multiLevel() {
        checkEval(let("x",num(44),let("y",num(2),let("z",num(0),vahe(var("x"),var("y"))))), 42);
        checkEval(let("x",num(1),let("y",num(5),sum("i",var("x"),var("y"),var("i")))), 15);
        checkEval(let("a",let("b",let("c",num(11),var("c")),vahe(var("b"),num(10))),let("b",num(10),vahe(var("a"),var("b")))), -9);
        checkEval(let("x",sum("i",num(1),num(4),var("i")),var("x")), 10);
        checkEval(sum("i",sum("j",num(1),num(100),num(0)),num(5),var("i")), 15);
        checkEval(vahe(vahe(num(10),var("x")),let("y",num(10),vahe(num(100),num(9)))), null);
        checkEval(let("x",vahe(var("x"),num(11)),var("x")), null);
    }

    @Test
    public void test11_eval_varia() {
        checkEval(let("x",num(5),let("x",vahe(var("x"),num(1)),var("x"))), 4);

        checkEval(vahe(let("x",num(10),var("x")),var("x")), null);
        checkEval(let("x",num(5),vahe(let("x",num(10),var("x")),var("x"))), 5);
        checkEval(let("x",num(5),vahe(var("x"), let("x",num(10),var("x")))), -5);

        checkEval(let("x",num(666),vahe(sum("i",num(0),num(3),sum("j",num(0),var("i"),vahe(var("i"),var("j")))),num(1))), 9);
        checkEval(let("x",num(10),let("x",num(1),let("y",num(8),sum("y",var("x"),num(20),vahe(var("y"),var("x")))))), 190);
    }


    private CMaStack st0 = new CMaStack(0, 0, 0);
    private CMaStack st1 = new CMaStack(1, 2, 3);
    private CMaStack st2 = new CMaStack(44, 100, -76);

    @Test
    public void test12_comp_literaalVahe() {
        checkComp(st0, num(0), 0);
        checkComp(st0, num(97519), 97519);
        checkComp(st0, vahe(num(10),num(5)), 5);
        checkComp(st0, vahe(vahe(num(10),num(5)),num(9)), -4);
        checkComp(st0, vahe(vahe(num(10),num(5)),vahe(num(9),num(22))), 18);
    }

    @Test
    public void test13_comp_globals() {
        checkComp(st0, var("x"), 0);
        checkComp(st1, var("x"), 1);
        checkComp(st1, var("y"), 2);
        checkComp(st2, vahe(num(10),var("z")), 86);
        checkComp(st1, vahe(vahe(num(10),var("y")),num(9)), -1);
    }

    @Test
    public void test14_eval_let() {
        checkComp(st0, let("a",num(5),var("a")), 5);
        checkComp(st0, let("kala",num(5),vahe(var("kala"),num(10))), -5);
        checkComp(st1, let("b", num(100),var("x")), 1);
        checkComp(st1, let("b", num(100),vahe(var("b"), var("x"))), 99);

        checkComp(st0, let("a",num(44), let("b",num(2), let("c",num(4),
                        vahe(var("a"),var("c"))))), 40);
        checkComp(st0, let("a",num(44), let("b", num(2), let("c", vahe(num(10), var("b")),
                vahe(var("a"),var("c"))))), 36);
        checkComp(st1, let("a",num(44), let("b", vahe(var("z"), num(2)), let("c", vahe(num(10), var("b")),
                vahe(var("a"),var("c"))))), 35);
    }

    @Test
    public void test15_eval_all() {
        checkComp(st0, sum("i",num(1),num(4),var("i")), 10);
        checkComp(st0, sum("i",num(1),num(4),num(1)), 4);
        checkComp(st0, sum("i",num(1),num(40),vahe(var("i"),num(10))), 420);
        checkComp(st1, sum("i",num(1),num(4),var("z")), 12);
        checkComp(st1, let("a", sum("i",num(1), num(4), var("i")),
                vahe(var("y"), var("a"))), -8);
        checkComp(st0, let("a", sum("i",num(1), num(4), var("i")),
                let("b", sum("i",num(1),num(4),num(2)),
                vahe(var("b"), var("a")))), -2);

    }

    private void checkComp(CMaStack stack, LetAvaldis expr, int expected) {
        CMaProgram program = LetAnalyzer.codeGen(expr);
        int actual = CMaInterpreter.run(program, stack).peek();
        assertEquals(expected, actual);
    }


    private void checkEval(LetAvaldis inputAst, Integer expected) {
        if (expected != null) {
            Integer actual = LetAnalyzer.eval(inputAst);
            assertEquals(expected, actual);
        } else {
            try {
                LetAnalyzer.eval(inputAst);
                fail("Meetod pidi viskama erindi!");
            } catch (RuntimeException ignore) {
            }
        }

    }

}
