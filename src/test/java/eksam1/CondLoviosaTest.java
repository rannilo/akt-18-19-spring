package eksam1;

import cma.CMaInterpreter;
import cma.CMaStack;
import eksam1.ast.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;

import static eksam1.CondLoviosa.codeGen;
import static eksam1.CondLoviosa.symbex;
import static eksam1.ast.CondNode.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CondLoviosaTest {


    @Test
    public void test01_codegen_novars() {
        checkCodegen(1, prog(decls(), il(1)));
        checkCodegen(1, prog(decls(), bl(true)));
        
        checkCodegen(3, prog(decls(), add(il(1), il(2))));
        checkCodegen(1, prog(decls(), or(bl(false), bl(true))));
        checkCodegen(1, prog(decls(), not(bl(false))));
        checkCodegen(-1, prog(decls(), neg(il(1))));
    }

    @Test
    public void test02_codegen_vars() {
        checkCodegen(2, prog(decls(iv("y")), var("y")), 2);
        checkCodegen(0, prog(decls(bv("b")), var("b")), 0);
        
        
        checkCodegen(5, prog(decls(iv("x")), add(var("x"), il(2))), 3);
        checkCodegen(10, prog(decls(iv("x")), add(var("x"), il(2))), 8);
        checkCodegen(0, prog(decls(bv("x")), or(bl(false), var("x"))),0);
        checkCodegen(1, prog(decls(bv("x")), or(bl(false), var("x"))),1);
    }

    @Test
    public void test03_codegen_ops() {
        checkCodegen(-1, prog(decls(iv("x")), neg(var("x"))), 1);
        checkCodegen(7, prog(decls(iv("x"), iv("z")), add(var("x"), var("z"))), 1, 6);
        checkCodegen(2, prog(decls(iv("x"), iv("y"), iv("z")), sub(div(var("z"), var("y")), var("x"))), 1, 2, 6);

        checkCodegen(1, prog(decls(bv("a"), bv("b")), and(var("a"), not(or(var("b"), bl(false))))), 1, 0);
        checkCodegen(0, prog(decls(bv("a"), bv("b")), and(var("a"), not(or(var("b"), bl(true))))), 1, 0);

        checkCodegen(1, prog(decls(iv("y")), eq(var("y"), il(2))), 2);
        checkCodegen(0, prog(decls(bv("b")), eq(var("b"), bl(true))), 0);
        checkCodegen(0, prog(decls(bv("a"), iv("z")), eq(var("a"), eq(var("z"), il(5)))), 1, 6);
        checkCodegen(1, prog(decls(bv("a"), iv("z")), eq(var("a"), eq(var("z"), il(6)))), 1, 6);
    }

    @Test
    public void test04_codegen_ifte() {
        checkCodegen(1, prog(decls(iv("x"), iv("y")), ifte(bl(true), var("x"), var("y"))), 1, 2);
        checkCodegen(2, prog(decls(iv("x"), iv("y")), ifte(bl(false), var("x"), var("y"))), 1, 2);
        checkCodegen(2, prog(decls(iv("x"), iv("y"), iv("z")), ifte(eq(var("x"), var("z")), var("z"), var("y"))), 1, 2, 6);

        checkCodegen(0, prog(decls(bv("a")), ifte(var("a"), bl(false), bl(true))), 1);
        checkCodegen(1, prog(decls(bv("b")), ifte(var("b"), bl(false), bl(true))), 0);

        checkCodegen(20, prog(decls(), ifte(bl(true), ifte(bl(false), il(10), il(20)), il(30))));
    }

    private void checkCodegen(int expected, CondNode ast, Integer... stack) {
        CMaStack initialStack = new CMaStack(asList(stack));
        assertEquals(expected, CMaInterpreter.run(codeGen(ast), initialStack).peek());
    }

    @Test
    public void test05_symbex_basic() {
        checkSymbex(prog(decls(), add(il(1), il(2))));
        checkSymbex(prog(decls(iv("x")), add(var("x"), il(2))));
        checkSymbex(prog(decls(iv("x")), add(var("error"), il(2))));
    }

    @Test
    public void test06_symbex_ifte() {
        checkSymbex(prog(decls(iv("x")), ifte(eq(var("x"), il(10)), var("error"), var("x"))));
        checkSymbex(prog(decls(iv("x")), ifte(eq(var("x"), il(10)), var("x"), var("error"))));
    }

    @Test
    public void test07_symbex_multiple_ifte() {
        checkSymbex(prog(decls(iv("x")), ifte(eq(var("x"), il(10)), ifte(eq(var("x"), il(11)), var("error"), il(4)), il(5))));
        checkSymbex(prog(decls(iv("x")), ifte(eq(var("x"), il(10)), ifte(eq(var("x"), il(11)), il(4), var("error")), il(5))));

        checkSymbex(prog(decls(iv("x")), add(ifte(eq(var("x"), il(10)), var("error"), il(5)), ifte(eq(var("x"), il(10)), il(5), var("error")))));
    }

    @Test
    public void test08_symbex_multiple_var() {
        checkSymbex(prog(decls(bv("a"), iv("x"), iv("y")), ifte(or(var("a"), not(eq(var("x"), var("y")))), var("error"), il(5))));
    }

    // NB! Selleks on korrektne eval vaja, mis on alusosa ülesanne...
    // Kui implementeerimata, siis testi moodle'is!
    private void checkSymbex(CondNode prog) {
        List<Map<String, Object>> envs = generateEnvs(prog);
        for (Map<String, Object> env : envs) {
            Boolean expect = doesItFail(() -> CondAlusosa.eval(prog, env));
            CondNode cond = symbex(prog);
            assertEquals("Program with input " + env + " should fail? (Your condition: " + cond + ")",
                    expect, CondAlusosa.eval(cond, env));
        }
    }

    private static List<Map<String, Object>> generateEnvs(CondNode prog) {
        List<Object> ivals = new LinkedList<>(Arrays.asList(42, 77, 300));
        List<Object> bvals = Arrays.asList(true, false);
        new CondAstVisitor<Void>() {
            @Override
            protected Void visit(CondLitInt num) {
                ivals.add(num.getValue());
                return null;
            }
        }.visit(prog);
        List<CondDecl> decls = ((CondProg) prog).getDecls();

        List<Map<String, Object>> maps = new LinkedList<>();
        maps.add(new HashMap<>());
        for (CondDecl decl : decls) {
            List<Object> vals = decl.isIntType() ? ivals : bvals;
            List<Map<String, Object>> newmaps = new LinkedList<>();
            for (Map<String, Object> map : maps) {
                for (Object val : vals) {
                    Map<String, Object> newmap = new HashMap<>(map);
                    newmap.put(decl.getName(), val);
                    newmaps.add(newmap);
                }
            }
            maps = newmaps;
        }
        return maps;
    }

    private Boolean doesItFail(Runnable f) {
        try {
            f.run();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static void main(String[] args) {
        CondNode prog = prog(decls(iv("x")), ifte(eq(var("x"), il(10)), var("error"), var("x")));
        List<Map<String, Object>> maps = generateEnvs(prog);
        System.out.println(maps);
        System.out.println(CondAlusosa.eval(prog, maps.get(3)));
    }

}
