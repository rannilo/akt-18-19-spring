package eksam1;

import eksam1.ast.CondDecl;
import eksam1.ast.CondNode;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;

import static eksam1.CondAlusosa.Type.*;
import static org.junit.Assert.*;
import static eksam1.ast.CondNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CondAlusosaTest {

    private List<CondDecl> evalDecls;
    private Map<String, Object> evalEnv;

    @Before
    public void setUp() {
        evalDecls = decls(bv("a"), bv("b"), iv("x"), iv("y"), iv("z"));

        evalEnv = new HashMap<>();

        evalEnv.put("a", true);
        evalEnv.put("b", false);

        evalEnv.put("x", 1);
        evalEnv.put("y", 2);
        evalEnv.put("z", 6);
    }

    @Test
    public void test01_unusedVars_basic() {
        checkUnusedVars(setOf(), prog(decls(), il(1)));
        checkUnusedVars(setOf("x"), prog(decls(iv("x")), il(1)));
        checkUnusedVars(setOf(), prog(decls(iv("x")), var("x")));
        checkUnusedVars(setOf("y"), prog(decls(iv("x"), iv("y")), var("x")));
    }

    @Test
    public void test02_unusedVars_ops() {
        checkUnusedVars(setOf("y", "z"), prog(decls(iv("x"), iv("y"), iv("z")), neg(var("x"))));
        checkUnusedVars(setOf("y"), prog(decls(iv("x"), iv("y"), iv("z")), add(var("x"), var("z"))));
        checkUnusedVars(setOf(), prog(decls(iv("x"), iv("y"), iv("z")), sub(div(var("x"), var("y")), var("z"))));

        checkUnusedVars(setOf("c"), prog(decls(bv("a"), bv("b"), bv("c")), and(var("a"), not(or(var("b"), bl(false))))));

        checkUnusedVars(setOf("b", "y"), prog(decls(bv("a"), bv("b"), iv("x"), iv("y")), eq(var("a"), eq(var("x"), il(5)))));
    }

    @Test
    public void test03_unusedVars_ifte() {
        checkUnusedVars(setOf("z"), prog(decls(iv("x"), iv("y"), iv("z")), ifte(bl(true), var("x"), var("y"))));
        checkUnusedVars(setOf("y"), prog(decls(iv("x"), iv("y"), iv("z")), ifte(bl(false), var("x"), var("z"))));
        checkUnusedVars(setOf(), prog(decls(iv("x"), iv("y"), iv("z")), ifte(eq(var("x"), var("z")), var("z"), var("y"))));
    }

    @SafeVarargs
    private static <T> Set<T> setOf(T... elems) {
        return new HashSet<>(Arrays.asList(elems));
    }

    private void checkUnusedVars(Set<String> expected, CondNode prog) {
        Set<String> actual = CondAlusosa.unusedVars(prog);
        assertEquals(expected, actual);
    }

    @Test
    public void test04_eval_basic() {
        checkEval(1, prog(evalDecls, il(1)));
        checkEval(true, prog(evalDecls, bl(true)));
        checkEval(2, prog(evalDecls, var("y")));
        checkEval(false, prog(evalDecls, var("b")));

        checkEval(null, prog(evalDecls, var("k"))); // deklareerimata muutuja
    }

    @Test
    public void test05_eval_ops() {
        checkEval(-1, prog(evalDecls, neg(var("x"))));
        checkEval(7, prog(evalDecls, add(var("x"), var("z"))));
        checkEval(2, prog(evalDecls, sub(div(var("z"), var("y")), var("x"))));

        checkEval(true, prog(evalDecls, and(var("a"), not(or(var("b"), bl(false))))));
        checkEval(false, prog(evalDecls, and(var("a"), not(or(var("b"), bl(true))))));

        checkEval(true, prog(evalDecls, eq(var("y"), il(2))));
        checkEval(false, prog(evalDecls, eq(var("b"), bl(true))));
        checkEval(false, prog(evalDecls, eq(var("a"), eq(var("z"), il(5)))));
        checkEval(true, prog(evalDecls, eq(var("a"), eq(var("z"), il(6)))));
    }

    @Test
    public void test06_eval_ifte() {
        checkEval(1, prog(evalDecls, ifte(bl(true), var("x"), var("y"))));
        checkEval(2, prog(evalDecls, ifte(bl(false), var("x"), var("y"))));
        checkEval(2, prog(evalDecls, ifte(eq(var("x"), var("z")), var("z"), var("y"))));

        checkEval(false, prog(evalDecls, ifte(var("a"), bl(false), bl(true))));
        checkEval(true, prog(evalDecls, ifte(var("b"), bl(false), bl(true))));

        checkEval(20, prog(evalDecls, ifte(bl(true), ifte(bl(false), il(10), il(20)), il(30))));
    }

    private void checkEval(Object expected, CondNode prog) {
        if (expected != null) {
            Object actual = CondAlusosa.eval(prog, Collections.unmodifiableMap(evalEnv));
            assertEquals(expected, actual);
        }
        else {
            try {
                CondAlusosa.eval(prog, Collections.unmodifiableMap(evalEnv));
                fail("Programm on vigane, aga eval ei visanud erindit");
            } catch (Exception e) {

            }
        }
    }

    @Test
    public void test07_typecheck_basic() {
        checkTypecheck(TInt, prog(decls(), il(2)));
        checkTypecheck(TBool, prog(decls(), bl(false)));
        checkTypecheck(TInt, prog(decls(iv("x")), var("x")));
        checkTypecheck(TBool, prog(decls(bv("x")), var("x")));
    }

    @Test
    public void test08_typecheck_ops() {
        checkTypecheck(TInt, prog(decls(), neg(il(1))));
        checkTypecheck(null, prog(decls(), not(il(1))));

        checkTypecheck(null, prog(decls(), neg(bl(true))));
        checkTypecheck(TBool, prog(decls(), not(bl(true))));


        checkTypecheck(TInt, prog(decls(), add(il(1), il(2))));
        checkTypecheck(TInt, prog(decls(), sub(il(1), il(2))));
        checkTypecheck(null, prog(decls(), and(il(1), il(2))));
        checkTypecheck(null, prog(decls(), or(il(1), il(2))));

        checkTypecheck(null, prog(decls(), add(bl(false), bl(true))));
        checkTypecheck(null, prog(decls(), sub(bl(false), bl(true))));
        checkTypecheck(TBool, prog(decls(), and(bl(false), bl(true))));
        checkTypecheck(TBool, prog(decls(), or(bl(false), bl(true))));

        checkTypecheck(null, prog(decls(), add(il(1), bl(false))));
        checkTypecheck(null, prog(decls(), eq(il(1), bl(false))));
        checkTypecheck(TBool, prog(decls(), eq(bl(false), bl(true))));
        checkTypecheck(TBool, prog(decls(), eq(il(1), il(5))));
    }

    @Test
    public void test09_typecheck_ifte() {
        checkTypecheck(TInt, prog(decls(), ifte(bl(true), il(1), il(2))));
        checkTypecheck(null, prog(decls(), ifte(il(5), il(1), il(2))));

        checkTypecheck(TBool, prog(decls(), ifte(bl(true), bl(false), bl(true))));
        checkTypecheck(null, prog(decls(), ifte(bl(true), il(1), bl(true))));
    }

    private void checkTypecheck(CondAlusosa.Type expectedType, CondNode prog) {
        try {
            CondAlusosa.Type actualType = CondAlusosa.typecheck(prog);
            if (expectedType != null)
                assertEquals(expectedType, actualType);
            else
                fail("Programm on vigane, aga typecheck ei visanud erindit");
        } catch (Exception e) {
            if (expectedType != null) {
                e.printStackTrace();
                fail("Programm ei ole vigane, aga typecheck viskas erindi: " + e);
            }
        }
    }
}
