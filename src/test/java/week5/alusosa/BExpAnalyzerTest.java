package week5.alusosa;

import com.google.common.collect.Sets;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week5.alusosa.bexp.DecisionDiagram;
import week5.alusosa.bexp.Stats;
import week5.alusosa.bexp.bexpAst.BExp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static week5.alusosa.bexp.BExpAnalyzer.*;
import static week5.alusosa.bexp.bexpAst.BExp.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class BExpAnalyzerTest {
    private BExp exp1 = not(not(not(var('A'))));
    private BExp exp2 = not(imp(var('B'), var('C')));
    private BExp exp3 = not(imp(or(var('E'), var('A')), not(var('A'))));
    private BExp exp4 = not(or(or(var('F'), var('D')), not(or(var('F'), var('C')))));
    private BExp exp5 = not(imp(or(or(var('A'), var('A')), imp(var('A'), var('A'))), or(var('A'), var('A'))));
    private BExp exp6 = or(imp(imp(var('D'), var('C')), or(var('D'), var('C'))), or(imp(var('B'), var('E')), not(var('C'))));


    private BExp exp10 = not(imp(or(var('B'), var('A')), or(var('C'), var('C'))));
    private BExp exp11 = or(not(or(var('E'), var('A'))), imp(or(var('B'), var('C')), imp(var('C'), var('C'))));
    private BExp exp12 = or(not(or(or(var('D'), var('E')), or(var('F'), var('E')))), not(imp(not(var('E')), not(var('A')))));
    private BExp exp13 = not(or(or(var('B'), var('D')), not(or(var('D'), var('A')))));
    private BExp exp14 = or(not(or(or(var('A'), var('A')), or(var('A'), var('A')))), or(or(var('A'), var('A')), imp(var('A'), var('A'))));
    private BExp exp15 = not(or(or(not(var('C')), or(var('B'), var('C'))), or(not(var('A')), or(var('C'), var('A')))));
    private BExp exp16 = not(not(not(not(or(not(var('A')), or(var('A'), var('A')))))));
    private BExp exp17 = or(or(not(not(var('B'))), or(not(var('B')), or(var('A'), var('B')))), not(or(not(not(var('A'))), or(or(var('B'), var('A')), or(var('B'), var('C'))))));
    private BExp exp18 = or(not(or(or(or(var('F'), var('E')), imp(var('B'), var('B'))), not(or(var('D'), var('B'))))), or(or(or(or(var('B'), var('C')), or(var('E'), var('B'))), or(var('F'), var('D'))), imp(not(var('B')), or(var('F'), var('E')))));
    private BExp exp19 = or(not(not(or(or(or(var('B'), var('A')), not(var('B'))), imp(or(var('B'), var('A')), not(not(var('A'))))))), or(or(or(var('A'), var('B')), or(var('A'), var('B'))), not(not(not(not(var('B')))))));
    private BExp exp20 = or(or(not(or(or(var('B'), var('A')), or(var('B'), var('B')))), imp(or(not(var('C')), or(var('A'), var('A'))), imp(not(var('C')), not(var('C'))))), or(not(or(or(var('C'), var('B')), or(var('A'), var('C')))), or(not(not(or(var('C'), var('B')))), not(or(or(var('A'), var('C')), not(var('C')))))));
    private BExp exp21 = not(or(or(or(imp(not(var('A')), or(var('A'), var('A'))), not(not(var('A')))), or(or(or(var('B'), var('B')), not(var('C'))), or(var('C'), var('B')))), or(imp(not(or(var('C'), var('A'))), not(imp(var('A'), var('A')))), not(not(or(var('C'), var('B')))))));
    private BExp exp22 = not(or(not(or(or(not(or(not(var('A')), or(var('A'), var('A')))), or(not(var('A')), not(var('A')))), not(or(or(not(var('A')), or(var('A'), var('A'))), or(var('A'), var('A')))))), not(or(not(imp(or(var('A'), var('A')), or(var('A'), var('A')))), or(or(or(var('A'), var('A')), not(imp(var('A'), var('A')))), or(not(not(var('A'))), not(or(var('A'), var('A')))))))));
    private BExp exp23 = not(or(or(or(not(or(var('B'), var('B'))), not(or(var('A'), var('B')))), not(imp(not(not(var('E'))), not(or(var('A'), var('A')))))), not(not(not(or(not(var('A')), not(var('B'))))))));
    private BExp exp24 = or(or(not(or(not(not(or(var('A'), var('A')))), imp(not(or(var('A'), var('A'))), imp(var('A'), var('A'))))), or(or(or(not(not(var('A'))), or(or(var('A'), var('A')), not(var('A')))), not(or(or(var('A'), var('A')), not(var('A'))))), not(not(or(var('A'), var('A')))))), not(not(not(or(not(or(var('A'), var('A'))), or(var('A'), var('A')))))));
    private BExp exp25 = not(or(or(or(not(or(or(or(var('A'), var('A')), not(var('A'))), not(or(var('A'), var('A'))))), or(not(or(var('A'), var('A'))), or(or(var('A'), var('A')), or(var('A'), var('A'))))), or(not(or(or(not(var('A')), or(var('A'), var('A'))), or(or(var('A'), var('A')), or(var('A'), var('A'))))), not(or(or(or(var('A'), var('A')), not(var('A'))), or(var('A'), var('A')))))), not(or(not(not(not(imp(var('A'), var('A'))))), imp(imp(or(var('A'), var('A')), or(var('A'), var('A'))), or(not(var('A')), or(var('A'), var('A'))))))));
    private Set<BExp> allExp = set(exp10, exp11, exp12, exp13, exp14, exp15, exp16, exp17, exp18, exp19, exp20, exp21, exp22, exp23, exp24, exp25);


    @Test
    public void test01_analyzeVars() {
        Stats stats;
        stats = analyze(var('A'));
        assertEquals(set('A'), stats.getVariables());
        assertFalse(stats.containsImp());

        stats = analyze(imp(var('A'), var('A')));
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
    }

    @Test
    public void test02_analyzeImp() {
        Stats stats;
        stats = analyze(var('A'));
        assertEquals(set('A'), stats.getVariables());
        assertFalse(stats.containsImp());

        stats = analyze(imp(var('A'), var('A')));
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
    }

    @Test
    public void test03_analyzeMore() {
        Stats stats = analyze(exp1);
        assertEquals(set('A'), stats.getVariables());
        assertFalse(stats.containsImp());

        stats = analyze(exp4);
        assertEquals(set('C', 'D', 'F'), stats.getVariables());
        assertFalse(stats.containsImp());

        stats = analyze(exp5);
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());

        stats = analyze(exp6);
        assertEquals(set('D', 'C', 'B', 'E'), stats.getVariables());
        assertTrue(stats.containsImp());
    }

    @Test
    public void test04_analyzeGen1() {
        Stats stats = analyze(exp10);
        assertEquals(set('A', 'B', 'C'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(exp11);
        assertEquals(set('A', 'B', 'C', 'E'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(exp12);
        assertEquals(set('A', 'D', 'E', 'F'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(exp13);
        assertEquals(set('A', 'B', 'D'), stats.getVariables());
        assertFalse(stats.containsImp());
        stats = analyze(exp14);
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(exp15);
        assertEquals(set('A', 'B', 'C'), stats.getVariables());
        assertFalse(stats.containsImp());
        stats = analyze(exp16);
        assertEquals(set('A'), stats.getVariables());
        assertFalse(stats.containsImp());
    }

    @Test
    public void test05_analyzeGen2() {
        Stats stats = analyze(exp17);
        assertEquals(set('A', 'B', 'C'), stats.getVariables());
        assertFalse(stats.containsImp());
        stats = analyze(exp18);
        assertEquals(set('B', 'C', 'D', 'E', 'F'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(exp19);
        assertEquals(set('A', 'B'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(exp20);
        assertEquals(set('A', 'B', 'C'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(exp21);
        assertEquals(set('A', 'B', 'C'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(exp22);
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(exp23);
        assertEquals(set('A', 'B', 'E'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(exp24);
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(exp25);
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
    }


    @Test
    public void test06_evalConst() {
        assertFalse(eval(var('A'), set()));
        assertTrue(eval(var('A'), set('A')));
        assertFalse(eval(var('B'), set('A')));
    }

    @Test
    public void test07_evalOps() {
        assertFalse(eval(var('A'), set()));
        assertTrue(eval(var('A'), set('A')));

        assertTrue(eval(not(var('A')), set()));
        assertFalse(eval(not(var('A')), set('A')));

        assertFalse(eval(or(var('A'), var('B')), set()));
        assertTrue(eval(or(var('A'), var('B')), set('A')));
        assertTrue(eval(or(var('A'), var('B')), set('B')));
        assertTrue(eval(or(var('A'), var('B')), set('A', 'B')));

        assertTrue(eval(imp(var('A'), var('B')), set()));
        assertFalse(eval(imp(var('A'), var('B')), set('A')));
        assertTrue(eval(imp(var('A'), var('B')), set('B')));
        assertTrue(eval(imp(var('A'), var('B')), set('A', 'B')));
    }

    @Test
    public void test08_evalCombo() {
        assertFalse(eval(var('A'), set()));
        assertTrue(eval(var('A'), set('A')));
        assertFalse(eval(var('B'), set('A')));

        assertTrue(eval(exp1, set()));
        assertFalse(eval(exp1, set('A')));

        // not(B -> C)
        assertFalse(eval(exp2, set()));
        assertTrue(eval(exp2, set('B')));
        assertFalse(eval(exp2, set('C')));
        assertFalse(eval(exp2, set('B', 'C')));

        assertTrue(eval(exp5, set()));
        assertFalse(eval(exp5, set('A')));
    }

    @Test
    public void test09_evalGen1() {
        assertFalse(eval(exp10, set()));
        assertTrue(eval(exp10, set('A')));
        assertTrue(eval(exp10, set('B')));
        assertTrue(eval(exp10, set('A', 'B')));
        assertFalse(eval(exp10, set('C')));
        assertFalse(eval(exp10, set('A', 'C')));
        assertFalse(eval(exp10, set('B', 'C')));
        assertFalse(eval(exp10, set('A', 'B', 'C')));
        assertTrue(eval(exp11, set()));
        assertTrue(eval(exp11, set('A')));
        assertTrue(eval(exp11, set('B')));
        assertTrue(eval(exp11, set('A', 'B')));
        assertTrue(eval(exp11, set('C')));
        assertTrue(eval(exp11, set('A', 'C')));
        assertTrue(eval(exp11, set('B', 'C')));
        assertTrue(eval(exp11, set('A', 'B', 'C')));
        assertTrue(eval(exp11, set('E')));
        assertTrue(eval(exp11, set('A', 'E')));
        assertTrue(eval(exp11, set('B', 'E')));
        assertTrue(eval(exp11, set('A', 'B', 'E')));
        assertTrue(eval(exp11, set('C', 'E')));
        assertTrue(eval(exp11, set('A', 'C', 'E')));
        assertTrue(eval(exp11, set('B', 'C', 'E')));
        assertTrue(eval(exp11, set('A', 'B', 'C', 'E')));
        assertTrue(eval(exp12, set()));
        assertTrue(eval(exp12, set('A')));
        assertFalse(eval(exp12, set('D')));
        assertTrue(eval(exp12, set('A', 'D')));
        assertFalse(eval(exp12, set('E')));
        assertFalse(eval(exp12, set('A', 'E')));
        assertFalse(eval(exp12, set('D', 'E')));
        assertFalse(eval(exp12, set('A', 'D', 'E')));
        assertFalse(eval(exp12, set('F')));
        assertTrue(eval(exp12, set('A', 'F')));
        assertFalse(eval(exp12, set('D', 'F')));
        assertTrue(eval(exp12, set('A', 'D', 'F')));
        assertFalse(eval(exp12, set('E', 'F')));
        assertFalse(eval(exp12, set('A', 'E', 'F')));
        assertFalse(eval(exp12, set('D', 'E', 'F')));
        assertFalse(eval(exp12, set('A', 'D', 'E', 'F')));
        assertFalse(eval(exp13, set()));
        assertTrue(eval(exp13, set('A')));
        assertFalse(eval(exp13, set('B')));
        assertFalse(eval(exp13, set('A', 'B')));
        assertFalse(eval(exp13, set('D')));
        assertFalse(eval(exp13, set('A', 'D')));
        assertFalse(eval(exp13, set('B', 'D')));
        assertFalse(eval(exp13, set('A', 'B', 'D')));
        assertTrue(eval(exp14, set()));
        assertTrue(eval(exp14, set('A')));
        assertFalse(eval(exp15, set()));
        assertFalse(eval(exp15, set('A')));
        assertFalse(eval(exp15, set('B')));
        assertFalse(eval(exp15, set('A', 'B')));
        assertFalse(eval(exp15, set('C')));
        assertFalse(eval(exp15, set('A', 'C')));
        assertFalse(eval(exp15, set('B', 'C')));
        assertFalse(eval(exp15, set('A', 'B', 'C')));
        assertTrue(eval(exp16, set()));
        assertTrue(eval(exp16, set('A')));
    }

    @Test
    public void test10_evalGen2() {
        assertTrue(eval(exp17, set()));
        assertTrue(eval(exp17, set('A')));
        assertTrue(eval(exp17, set('B')));
        assertTrue(eval(exp17, set('A', 'B')));
        assertTrue(eval(exp17, set('C')));
        assertTrue(eval(exp17, set('A', 'C')));
        assertTrue(eval(exp17, set('B', 'C')));
        assertTrue(eval(exp17, set('A', 'B', 'C')));
        assertFalse(eval(exp18, set()));
        assertTrue(eval(exp18, set('B')));
        assertTrue(eval(exp18, set('C')));
        assertTrue(eval(exp18, set('B', 'C')));
        assertTrue(eval(exp18, set('D')));
        assertTrue(eval(exp18, set('B', 'D')));
        assertTrue(eval(exp18, set('C', 'D')));
        assertTrue(eval(exp18, set('B', 'C', 'D')));
        assertTrue(eval(exp18, set('E')));
        assertTrue(eval(exp18, set('B', 'E')));
        assertTrue(eval(exp18, set('C', 'E')));
        assertTrue(eval(exp18, set('B', 'C', 'E')));
        assertTrue(eval(exp18, set('D', 'E')));
        assertTrue(eval(exp18, set('B', 'D', 'E')));
        assertTrue(eval(exp18, set('C', 'D', 'E')));
        assertTrue(eval(exp18, set('B', 'C', 'D', 'E')));
        assertTrue(eval(exp18, set('F')));
        assertTrue(eval(exp18, set('B', 'F')));
        assertTrue(eval(exp18, set('C', 'F')));
        assertTrue(eval(exp18, set('B', 'C', 'F')));
        assertTrue(eval(exp18, set('D', 'F')));
        assertTrue(eval(exp18, set('B', 'D', 'F')));
        assertTrue(eval(exp18, set('C', 'D', 'F')));
        assertTrue(eval(exp18, set('B', 'C', 'D', 'F')));
        assertTrue(eval(exp18, set('E', 'F')));
        assertTrue(eval(exp18, set('B', 'E', 'F')));
        assertTrue(eval(exp18, set('C', 'E', 'F')));
        assertTrue(eval(exp18, set('B', 'C', 'E', 'F')));
        assertTrue(eval(exp18, set('D', 'E', 'F')));
        assertTrue(eval(exp18, set('B', 'D', 'E', 'F')));
        assertTrue(eval(exp18, set('C', 'D', 'E', 'F')));
        assertTrue(eval(exp18, set('B', 'C', 'D', 'E', 'F')));
        assertTrue(eval(exp19, set()));
        assertTrue(eval(exp19, set('A')));
        assertTrue(eval(exp19, set('B')));
        assertTrue(eval(exp19, set('A', 'B')));
        assertTrue(eval(exp20, set()));
        assertTrue(eval(exp20, set('A')));
        assertTrue(eval(exp20, set('B')));
        assertTrue(eval(exp20, set('A', 'B')));
        assertTrue(eval(exp20, set('C')));
        assertTrue(eval(exp20, set('A', 'C')));
        assertTrue(eval(exp20, set('B', 'C')));
        assertTrue(eval(exp20, set('A', 'B', 'C')));
        assertFalse(eval(exp21, set()));
        assertFalse(eval(exp21, set('A')));
        assertFalse(eval(exp21, set('B')));
        assertFalse(eval(exp21, set('A', 'B')));
        assertFalse(eval(exp21, set('C')));
        assertFalse(eval(exp21, set('A', 'C')));
        assertFalse(eval(exp21, set('B', 'C')));
        assertFalse(eval(exp21, set('A', 'B', 'C')));
        assertTrue(eval(exp22, set()));
        assertFalse(eval(exp22, set('A')));
        assertFalse(eval(exp23, set()));
        assertFalse(eval(exp23, set('A')));
        assertTrue(eval(exp23, set('B')));
        assertFalse(eval(exp23, set('A', 'B')));
        assertFalse(eval(exp23, set('E')));
        assertFalse(eval(exp23, set('A', 'E')));
        assertTrue(eval(exp23, set('B', 'E')));
        assertFalse(eval(exp23, set('A', 'B', 'E')));
        assertTrue(eval(exp24, set()));
        assertTrue(eval(exp24, set('A')));
        assertFalse(eval(exp25, set()));
        assertFalse(eval(exp25, set('A')));
    }


    // NB! Järgmised testid on ainult adekvaatsed, kui analyze ja eval on õigesti defineeritud.

    @Test
    public void test11_transformBasic() {
        check(imp(var('A'), var('B')));
    }

    @Test
    public void test12_transformMore() {
        check(exp1);
        check(exp2);
        check(exp3);
        check(exp4);
        check(exp5);
        check(exp6);
    }

    @Test
    public void test13_TransformAll() {
        for (BExp exp : allExp) check(exp);
    }

    @Test
    public void test14_Diagram() {
        checkDiagram(exp1);
        checkDiagram(exp2);
        checkDiagram(exp3);
        checkDiagram(exp4);
        checkDiagram(exp5);
        checkDiagram(exp6);
    }

    @Test
    public void test15_DiagramAllAll() {
        for (BExp exp : allExp) checkDiagram(exp);
    }



    private void check(BExp exp) {
        Stats origStats = analyze(exp);
        BExp transformed = transform(exp);
        Stats transStats = analyze(transformed);
        assertEquals(origStats.getVariables(), transStats.getVariables());
        assertFalse(transStats.containsImp());
        for (Set<Character> tv : Sets.powerSet(transStats.getVariables())) {
            assertEquals(eval(exp, tv), eval(transformed, tv));
        }
    }

    private void checkDiagram(BExp exp) {
        Stats stats = analyze(exp);
        DecisionDiagram diagram = createDiagram(exp);
        for (Set<Character> tv : Sets.powerSet(stats.getVariables())) {
            assertEquals(eval(exp, tv), diagram.eval(tv));
        }
    }

    @SafeVarargs
    private final <T> Set<T> set(T... characters) {
        return new HashSet<T>(Arrays.asList(characters));
    }

}