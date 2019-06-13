package eksam2;

import cma.CMaInterpreter;
import cma.CMaStack;
import eksam2.ast.BologImp;
import eksam2.ast.BologNode;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static eksam2.BologLoviosa.codeGen;
import static eksam2.ast.BologNode.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BologLoviosaTest {


    @Test
    public void test01_codegen_basic() {
        checkCodegen(1, tv(true));
        checkCodegen(0, tv(false));
        checkCodegen(0, var("P"), 0);
        checkCodegen(1, var("P"), 1);
    }

    @Test
    public void test02_codegen_ops() {
        checkCodegen(1, nand(var("P"), var("Q")), 0, 0);
        checkCodegen(1, nand(var("P"), var("Q")), 1, 0);
        checkCodegen(1, nand(var("P"), var("Q")), 0, 1);
        checkCodegen(0, nand(var("P"), var("Q")), 1, 1);
    }


    @Test
    public void test03_codegen_ops() {
        checkCodegen(1, nand(var("P"), var("Q")), 0, 0);
        checkCodegen(1, nand(var("P"), var("Q")), 1, 0);
        checkCodegen(1, nand(var("P"), var("Q")), 0, 1);
        checkCodegen(0, nand(var("P"), var("Q")), 1, 1);
    }

    private void checkCodegen(int expected, BologNode ast, Integer... stack) {
        CMaStack initialStack = new CMaStack(asList(stack));
        assertEquals(expected, CMaInterpreter.run(codeGen(ast), initialStack).peek());
    }


    @Test
    public void test04_checkmodel() {
        checkModel(setOf("X"), imp(var("X")));
        checkModel(setOf("X", "X"), imp(var("X")), imp(var("X"), var("Y")));
        checkModel(setOf("X", "Y"), imp(var("X")), imp(var("Y"), var("X")));
    }

    @Test
    public void test05_checkmodel_more() {
        checkModel(setOf("X"), imp(var("X"), tv(true)));
        checkModel(setOf(), imp(var("X"), var("X")));
        checkModel(setOf("X", "Y", "Z"),
                imp(var("X"), var("Y")),
                imp(var("Y")), imp(var("Z"), var("X"), var("Y")),
                imp(var("Z"), var("P")));
        checkModel(setOf("Z"),
                imp(var("X"), var("Y")),
                imp(var("Y"), var("X")),
                imp(var("Z"), var("X"), var("Y")),
                imp(var("Z")));
    }

    @Test
    public void test06_sanity() {
        Set<BologImp> imps = new HashSet<>();
        for (char i = 'A'; i < 'Y'; i++) {
            imps.add((BologImp) imp(var(Character.toString(i)), var(Character.toString(i))));
        }
        assertEquals(setOf(), BologLoviosa.leastModel(imps));

        Set<String> chars = new HashSet<>();
        for (char i = 'A'; i < 'Y'; i++) {
            imps.add((BologImp) imp(var(Character.toString(i))));
            chars.add(Character.toString(i));
        }
        assertEquals(chars, BologLoviosa.leastModel(imps));
    }

    private static void checkModel(Set<String> model, BologNode... imp) {
        Set<BologNode> nodes = new HashSet<>(Arrays.asList(imp));
        Set<BologImp> nodecast = new HashSet<>();
        for (BologNode node : nodes) nodecast.add((BologImp) node);
        assertEquals(model, BologLoviosa.leastModel(nodecast));
    }

    @SafeVarargs
    private static <T> Set<T> setOf(T... elems) {
        return new HashSet<>(Arrays.asList(elems));
    }

}
