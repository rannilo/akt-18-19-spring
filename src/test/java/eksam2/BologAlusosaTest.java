package eksam2;

import com.google.common.collect.Sets;
import eksam2.ast.BologAstVisitor;
import eksam2.ast.BologImp;
import eksam2.ast.BologNode;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static eksam2.ast.BologNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BologAlusosaTest {

    
    @Test
    public void test01_conclusionvars1() {
        checkCVars(setOf("X"), imp(var("X")));
        checkCVars(setOf("X", "Z"), nand(nand(tv(true), imp(var("X"), var("Y"))), nand(tv(true), imp(var("Z"), var("P")))));
    }

    @Test
    public void test02_conclusionvars2() {
        checkCVars(setOf(), var("X"));
        checkCVars(setOf("X", "Y"), imp(nand(var("X"), var("Y"))));
        checkCVars(setOf("X"), nand(imp(var("X")), var("Y")));
    }


    @SafeVarargs
    private static <T> Set<T> setOf(T... elems) {
        return new HashSet<>(Arrays.asList(elems));
    }

    private static void checkCVars(Set<String> expected, BologNode prog) {
        Set<String> actual = new BologAlusosa().conclusionVars(prog);
        assertEquals(expected, actual);
    }

    @Test
    public void test03_eval1() {
        checkEval(true, var("X"), setOf("X"));
        checkEval(false, var("X"), setOf());

        checkEval(true, nand(var("P"), var("Q")), setOf());
        checkEval(true, nand(var("P"), var("Q")), setOf("P"));
        checkEval(true, nand(var("P"), var("Q")), setOf("Q"));
        checkEval(false, nand(var("P"), var("Q")), setOf("P", "Q"));

        checkEval(true, nand(var("P"), var("Q")), setOf());
        checkEval(true, nand(var("P"), var("Q")), setOf("P"));
        checkEval(true, nand(var("P"), var("Q")), setOf("Q"));
        checkEval(false, nand(var("P"), var("Q")), setOf("P", "Q"));
    }

    @Test
    public void test04_eval2() {
        checkEval(true, imp(var("X")), setOf("X"));
        checkEval(false, imp(var("X")), setOf());
        checkEval(true, imp(var("X"), var("Y"), var("Z")), setOf("Y"));
        checkEval(true, imp(var("X"), var("Y"), var("Z")), setOf("Z", "Y", "X"));
        checkEval(false, imp(var("X"), var("Y"), var("Z")), setOf("Z", "Y"));

        checkEval(false, imp(nand(tv(true), nand(var("X"), var("P"))), var("P"),
                nand(nand(tv(true), var("P")),
                        nand(tv(true), var("Q")))),
                setOf("P", "Q"));
    }

    private static void checkEval(Boolean expected, BologNode prog, Set<String> tv) {
        Boolean actual = new BologAlusosa().eval(tv, prog);
        assertEquals(expected, actual);
    }

    @Test
    public void test05_toNand1() {
        checkToNand(imp(var("X")));
        checkToNand(imp(var("X"), var("Y"), var("Z")));
        checkToNand(imp(var("X"), var("Y"), var("Z")));
    }

    @Test
    public void test06_toNand2() {
        checkToNand(imp(nand(tv(true), nand(var("X"), var("P"))), var("P"),
                nand(nand(tv(true), var("P")),
                        nand(tv(true), var("Q")))));
    }




    // Eval peab olema korrektne!
    private static void checkToNand(BologNode prog) {
        BologNode nandExpr = new BologAlusosa().toNandExpr((BologImp) prog);
        assertTrue(noConditional(nandExpr));
        Set<String> vars = new HashSet<>(Arrays.asList("X", "Y", "Z", "P", "Q"));
        for (Set<String> tv : Sets.powerSet(vars)) {
            boolean original = new BologAlusosa().eval(tv, prog);
            boolean transformed = new BologAlusosa().eval(tv, nandExpr);
            assertEquals(original, transformed);
        }
    }

    private static boolean noConditional(BologNode nandExpr) {
        final boolean[] ok = {true};
        new BologAstVisitor<Void>() {
            @Override
            protected Void visit(BologImp imp) {
                ok[0] = false;
                return null;
            }
        }.visit(nandExpr);
        return ok[0];
    }

}
