package week11.loviosa;

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
    public void test01_eval_literaalVahe() {
        checkEval(num(0), 0);
        checkEval(num(97519), 97519);
        checkEval(vahe(num(10),num(5)), 5);
        checkEval(vahe(vahe(num(10),num(5)),num(9)), -4);
        checkEval(vahe(vahe(num(10),num(5)),vahe(num(9),num(22))), 18);
    }

    @Test
    public void test02_eval_let() {
        checkEval(let("x",num(5),var("x")), 5);
        checkEval(let("x",num(5),vahe(var("x"),num(10))), -5);
        checkEval(let("x",vahe(num(10),num(3)),vahe(var("x"),num(100))), -93);
        checkEval(let("x",num(100),num(5)), 5);
        checkEval(vahe(num(55),let("x",num(100),num(5))), 50);
        checkEval(var("a"), null);
        checkEval(let("x",num(100),vahe(var("z"),var("x"))), null);
    }

    @Test
    public void test03_eval_sum() {
        checkEval(sum("i",num(1),num(4),var("i")), 10);
        checkEval(sum("i",num(1),num(4),num(1)), 4);
        checkEval(sum("i",num(1),num(40),vahe(var("i"),num(10))), 420);
        checkEval(sum("x",vahe(num(1),num(1)),vahe(vahe(num(10),num(1)),num(1)),var("i")), null);
        checkEval(sum("x",num(1),num(4),var("y")), null);
    }

    @Test
    public void test04_eval_multiLevel() {
        checkEval(let("x",num(44),let("y",num(2),let("z",num(0),vahe(var("x"),var("y"))))), 42);
        checkEval(let("x",num(1),let("y",num(5),sum("i",var("x"),var("y"),var("i")))), 15);
        checkEval(let("a",let("b",let("c",num(11),var("c")),vahe(var("b"),num(10))),let("b",num(10),vahe(var("a"),var("b")))), -9);
        checkEval(let("x",sum("i",num(1),num(4),var("i")),var("x")), 10);
        checkEval(sum("i",sum("j",num(1),num(100),num(0)),num(5),var("i")), 15);
        checkEval(vahe(vahe(num(10),var("x")),let("y",num(10),vahe(num(100),num(9)))), null);
        checkEval(let("x",vahe(var("x"),num(11)),var("x")), null);
    }

    @Test
    public void test05_eval_varia() {
        test01_eval_literaalVahe();
        test02_eval_let();
        test03_eval_sum();
        test04_eval_multiLevel();

        checkEval(let("x",num(5),let("x",vahe(var("x"),num(1)),var("x"))), 4);

        checkEval(vahe(let("x",num(10),var("x")),var("x")), null);
        checkEval(let("x",num(5),vahe(let("x",num(10),var("x")),var("x"))), 5);
        checkEval(let("x",num(5),vahe(var("x"), let("x",num(10),var("x")))), -5);

        checkEval(let("x",num(666),vahe(sum("i",num(0),num(3),sum("j",num(0),var("i"),vahe(var("i"),var("j")))),num(1))), 9);
        checkEval(let("x",num(10),let("x",num(1),let("y",num(8),sum("y",var("x"),num(20),vahe(var("y"),var("x")))))), 190);
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

    @Test
    public void test01() {
        assertTrue(onVaja("x", var("x")));
        assertFalse(onVaja("x", num(0)));
        assertFalse(onVaja("x", var("y")));
        assertTrue(onVaja("kala", vahe(var("x"), var("kala"))));
        assertTrue(onVaja("kala", vahe(var("kala"), var("y"))));
        assertFalse(onVaja("kala", vahe(var("x"), var("y"))));
    }

    @Test
    public void test02() {
        assertTrue(onVaja("x", let("y", num(10), var("x"))));
        assertTrue(onVaja("x", let("y", var("x"), var("y"))));
        assertFalse(onVaja("z", let("y", num(10), var("x"))));
        assertFalse(onVaja("z", let("y", var("x"), var("y"))));
    }


    @Test
    public void test03() {
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
    public void test04() {
        assertEquals(var("y"), optimeeri(let("x", num(5), var("y"))));
        assertEquals(let("x", num(5), var("x")), optimeeri(let("x", num(5), var("x"))));
    }

    @Test
    public void test05() {
        assertEquals(let("x",num(5),let("z",var("x"),var("z"))),
                optimeeri(let("x",num(5),let("y",num(10),let("z",var("x"),var("z"))))));
        assertEquals(let("a", var("y"), var("a")),
                optimeeri(let("a", let("x", num(5), var("y")), var("a"))));
    }

    @Test
    public void test06() {
        assertEquals(var("z"), optimeeri(let("x",num(10),let("y",var("x"),var("z")))));
        assertEquals(var("y"), optimeeri(let("a", let("a", num(10), var("a")), var("y"))));
        assertEquals(let("a", var("a"), vahe(var("b"), var("a"))),
                optimeeri(let("a", let("b", var("x"), var("a")), vahe(var("b"), var("a")))));
    }


}
