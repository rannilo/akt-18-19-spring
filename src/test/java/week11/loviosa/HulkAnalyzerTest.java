package week11.loviosa;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import cma.CMaUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week9.pohiosa.hulk.hulkAst.HulkNode;
import week9.pohiosa.hulk.hulkAst.HulkProgramm;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static week11.loviosa.HulkAnalyzer.run;
import static week9.pohiosa.hulk.hulkAst.HulkNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class HulkAnalyzerTest {
    private Map<Character, Set<Character>> env = new HashMap<>();
    private HulkProgramm prog;
    private Map<Character, Set<Character>> runningEnv;

    @Before
    public void setUp() {
        env.put('A', set('a'));
        env.put('B', set('b'));
        env.put('X', set('a', 'b', 'c'));
    }

    @Test
    public void test01_eval_literaalMuutuja() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',lit(),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set());
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',lit('x'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('C',set('x'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('D',var('A'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('D',set('a'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('D',lit('x','y','z'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('D',set('x','y','z'));
        checkEval(prog, runningEnv);
    }

    @Test
    public void test02_eval_avaldis() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(lit('x'),lit('y'),'+'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('x','y'));
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('G',tehe(var('X'),lit('b'),'-'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('G',set('a','c'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('H',tehe(var('X'),var('A'),'&'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('H',set('a'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('H',tehe(tehe(var('X'),var('A'),'&'),var('B'),'&'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('H',set());
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('H',tehe(tehe(var('X'),var('A'),'+'),var('B'),'-'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('H',set('a','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('V',tehe(tehe(var('X'),lit('a'),'-'),lit('b'),'-'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('V',set('c'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('V',tehe(var('X'),tehe(lit('a'),lit('b'),'-'),'-'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('V',set('b','c'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('B',tehe(tehe(tehe(tehe(lit('x'),lit('u','y'),'+'),var('A'),'+'),var('V'),'+'),lit('k','m'),'+'),null));
        checkEval(prog, null);
    }

    @Test
    public void test03_eval_tingimus() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('B'),ting(var('A'),var('X'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('b'));
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('B'),ting(var('X'),var('A'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',lit('u','v'),ting(var('A'),var('A'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('u','v'));
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(tehe(var('A'),var('B'),'+'),var('C'),'&'),ting(var('A'),var('A'))));
        checkEval(prog, null);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('X'),lit('a','y'),'-'),ting(lit('x'),lit('x','y'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('b','c'));
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',tehe(var('A'),lit('x','y'),'+'),ting(tehe(tehe(lit('x'),var('A'),'&'),var('X'),'+'),tehe(tehe(lit('a','b','c','x','y'),tehe(lit('y'),var('A'),'+'),'-'),var('A'),'+'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('C',set('a','x','y'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',tehe(var('A'),lit('x','y'),'+'),ting(tehe(tehe(lit('x'),var('A'),'&'),var('X'),'+'),tehe(lit('a','b','c','x','y'),tehe(lit('y'),var('A'),'+'),'-'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(tehe(var('A'),var('B'),'+'),var('C'),'&'),ting(tehe(var('Y'),var('X'),'+'),var('A'))));
        checkEval(prog, null);
    }

    @Test
    public void test04_eval_laused() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',var('B'),null));
        prog.lisaLause(lause('D',var('C'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('C',set('b'));
        runningEnv.put('D',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('A'),null));
        prog.lisaLause(lause('B',var('X'),null));
        prog.lisaLause(lause('C',lit('a'),null));
        prog.lisaLause(lause('D',lit('x','y','z'),null));
        prog.lisaLause(lause('A',var('B'),ting(var('A'),var('A'))));
        prog.lisaLause(lause('A',tehe(var('A'),lit('a','y'),'-'),ting(tehe(lit('x'),var('D'),'&'),lit('x','y'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('b','c'));
        runningEnv.put('B',set('a','b','c'));
        runningEnv.put('C',set('a'));
        runningEnv.put('D',set('x','y','z'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',var('B'),null));
        prog.lisaLause(lause('D',tehe(var('C'),var('F'),'+'),null));
        checkEval(prog, null);
    }

    @Test
    public void test05_eval_varia() {
        test01_eval_literaalMuutuja();
        test02_eval_avaldis();
        test03_eval_tingimus();
        test04_eval_laused();

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('C'),null));
        checkEval(prog, null);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',lit('x','y'),ting(lit(),lit())));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('x','y'));
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x','y'),'+'),ting(tehe(var('A'),var('B'),'&'),tehe(tehe(var('X'),lit('a'),'&'),var('A'),'-'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a','x','y'));
        runningEnv.put('B',set('b'));
        checkEval(prog, runningEnv);
    }



    @Test
    public void test01_trivialAnalysis() {
        // A := ({a}+{b})
        // B := {c}
        HulkProgramm prog1 = prog(new ArrayList<>());
        prog1.lisaLause(lause('A', tehe(lit('a'), lit('b'), '+'), null));
        prog1.lisaLause(lause('B', lit('c'), null));
        Assert.assertTrue(HulkAnalyzer.isValidHulkNode(prog1));

        // A := {a}
        // B := C
        HulkProgramm prog2 = prog(new ArrayList<>());
        prog2.lisaLause(lause('A', lit('a'), null));
        prog2.lisaLause(lause('B', var('C'), null));
        Assert.assertFalse(HulkAnalyzer.isValidHulkNode(prog2));
    }

    @Test
    public void test02_varsAnalysis() {
        // A := {a}
        // B := (A+{b})
        HulkProgramm prog1 = prog(new ArrayList<>());
        prog1.lisaLause(lause('A', lit('a'), null));
        prog1.lisaLause(lause('B', tehe(var('A'), lit('b'), '+'), null));
        Assert.assertTrue(HulkAnalyzer.isValidHulkNode(prog1));

        // A := (B&{a, b})
        // B := (A-{a})
        HulkProgramm prog2 = prog(new ArrayList<>());
        prog2.lisaLause(lause('A', tehe(var('B'), lit('a', 'b'), '&'), null));
        prog2.lisaLause(lause('B', tehe(var('A'), lit('a'), '-'), null));
        Assert.assertFalse(HulkAnalyzer.isValidHulkNode(prog2));

        // A := ({a, b}&{a})
        // B := ((A-B)+{c})
        HulkProgramm prog3 = prog(new ArrayList<>());
        prog3.lisaLause(lause('A', tehe(lit('a','b'), lit('a'), '&'), null));
        prog3.lisaLause(lause('B', tehe(tehe(var('A'), var('B'), '-'), lit('c'), '+'), null));
        Assert.assertFalse(HulkAnalyzer.isValidHulkNode(prog3));
    }

    @Test
    public void test03_conditionAnalysis() {
        // A := A | {a} subset {b, c}
        HulkProgramm prog1 = prog(new ArrayList<>());
        prog1.lisaLause(lause('A', var('A'), ting(lit('a'), lit('b', 'c'))));
        Assert.assertFalse(HulkAnalyzer.isValidHulkNode(prog1));

        // A := {b} | {a} subset {a, b, c}
        // B := (A-{a})
        HulkProgramm prog2 = prog(new ArrayList<>());
        prog2.lisaLause(lause('A', lit('b'), ting(lit('a'), lit('a', 'b', 'c'))));
        prog2.lisaLause(lause('B', tehe(var('A'), lit('a'), '-'), null));
        Assert.assertFalse(HulkAnalyzer.isValidHulkNode(prog2));

        // A := ({a}+{b}) | {a} subset {a, b, c}
        HulkProgramm prog3 = prog(new ArrayList<>());
        prog3.lisaLause(lause('A', tehe(lit('a'), lit('b'), '+'),
                ting(lit('a'), lit('a', 'b', 'c'))));
        Assert.assertTrue(HulkAnalyzer.isValidHulkNode(prog3));

        // A := {a, b, c}
        // B := (A+{a})
        HulkProgramm prog4 = prog(new ArrayList<>());
        prog4.lisaLause(lause('A', lit('a', 'b', 'c'), null));
        prog4.lisaLause(lause('B', tehe(var('A'), lit('a'), '+'), null));
        Assert.assertTrue(HulkAnalyzer.isValidHulkNode(prog4));
    }

    @Test
    public void test04_mixedAnalysis() {
        // A := {d} | {a} subset {a, b}
        // B := ({b}&{c})
        // A := (B-{a})
        // C := ((A&B)-{c}) | A subset (A&B)
        // B := {a, b} | {a} subset {a, b}
        // A := (A-B)
        HulkProgramm prog1 = prog(new ArrayList<>());
        prog1.lisaLause(lause('A', lit('d'), ting(lit('a'), lit('a', 'b'))));
        prog1.lisaLause(lause('B', tehe(lit('b'), lit('c'), '&'), null));
        prog1.lisaLause(lause('A', tehe(var('B'), lit('a'), '-'), null));
        prog1.lisaLause(lause('C', tehe(tehe(var('A'), var('B'), '&'), lit('c'), '-'),
                ting(var('A'), tehe(var('A'), var('B'), '&'))));
        prog1.lisaLause(lause('B', lit('a','b'), ting(lit('a'), lit('a', 'b'))));
        prog1.lisaLause(lause('A', tehe(var('A'), var('B'), '-'), null));
        Assert.assertTrue(HulkAnalyzer.isValidHulkNode(prog1));

        // A := {a} | {b} subset {a, b}
        // B := {b}
        // A := B | {a} subset {a, b}
        // C := (B&{c})
        // C := ((B&C)-{a}) | B subset A
        HulkProgramm prog2 = prog(new ArrayList<>());
        prog2.lisaLause(lause('A', lit('a'), ting(lit('b'), lit('a', 'b'))));
        prog2.lisaLause(lause('B', lit('b'), null));
        prog2.lisaLause(lause('A', var('B'), ting(lit('a'), lit('a','b'))));
        prog2.lisaLause(lause('C', tehe(var('B'), lit('c'), '&'), null));
        prog2.lisaLause(lause('C', tehe(tehe(var('B'), var('C'), '&'), lit('a'), '-'),
                ting(var('B'), var('A'))));
        Assert.assertFalse(HulkAnalyzer.isValidHulkNode(prog2));
    }

    @Test
    public void test05_processTrivial() {
        // A := (B+C) | B subset {}
        // B := {}
        HulkProgramm prog1 = prog(new ArrayList<>());
        prog1.lisaLause(lause('A', tehe(var('B'), var('C'), '+'),
                ting(var('B'), lit())));
        prog1.lisaLause(lause('B', lit(), null));

        Assert.assertEquals(prog1, HulkAnalyzer.processEmptyLiterals(prog1));
    }

    @Test
    public void test06_processPlus() {
        // A := ((A+B)+{})
        // B := {b} | {b} subset ({}+{})
        HulkProgramm prog2 = prog(new ArrayList<>());
        prog2.lisaLause(lause('A', tehe(tehe(var('A'), var('B'), '+'), lit(), '+'), null));
        prog2.lisaLause(lause('B', lit('b'),
                ting(lit('b'), tehe(lit(), lit(), '+'))));

        HulkProgramm prog2expected = prog(new ArrayList<>());
        prog2expected.lisaLause(lause('A', tehe(var('A'), var('B'), '+'), null));
        prog2expected.lisaLause(lause('B', lit('b'), ting(lit('b'), lit())));

        Assert.assertEquals(prog2expected, HulkAnalyzer.processEmptyLiterals(prog2));


        // A := (({}+{})+{})
        HulkProgramm prog3 = prog(new ArrayList<>());
        prog3.lisaLause(lause('A', tehe(tehe(lit(), lit(), '+'), lit(), '+'), null));

        HulkProgramm prog3expected = prog(new ArrayList<>());
        prog3expected.lisaLause(lause('A', lit(), null));

        Assert.assertEquals(prog3expected, HulkAnalyzer.processEmptyLiterals(prog3));


        // A := (((A&B)+{})-C)
        // B := ({}+{}) | {a} subset (({a, b}-C)+{})
        HulkProgramm prog4 = prog(new ArrayList<>());
        prog4.lisaLause(lause('A', tehe(tehe(tehe(var('A'), var('B'), '&'), lit(), '+'), var('C'), '-'), null));
        prog4.lisaLause(lause('B', tehe(lit(), lit(), '+'),
                ting(lit('a'), tehe(tehe(lit('a', 'b'), var('C'), '-'), lit(), '+'))));

        HulkProgramm prog4expected = prog(new ArrayList<>());
        prog4expected.lisaLause(lause('A', tehe(tehe(var('A'), var('B'), '&'), var('C'), '-'), null));
        prog4expected.lisaLause(lause('B', lit(),
                ting(lit('a'), tehe(lit('a', 'b'), var('C'), '-'))));
        Assert.assertEquals(prog4expected, HulkAnalyzer.processEmptyLiterals(prog4));
    }

    @Test
    public void test07_processIntersection() {
        // A := ({}&B) | (({a}-B)+C) subset ({a}&{})
        HulkProgramm prog1 = prog(new ArrayList<>());
        prog1.lisaLause(lause('A', tehe(lit(), var('B'), '&'),
                ting(tehe(tehe(lit('a'), var('B'), '-'), var('C'), '+'), tehe(lit('a'), lit(), '&'))));

        HulkProgramm prog1expected = prog(new ArrayList<>());
        prog1expected.lisaLause(lause('A', lit(),
                ting(tehe(tehe(lit('a'), var('B'), '-'), var('C'), '+'), lit())));

        Assert.assertEquals(prog1expected, HulkAnalyzer.processEmptyLiterals(prog1));


        // A := (((A+B)-C)&{}) | {a} subset ((B&C)&{a, b})
        HulkProgramm prog2 = prog(new ArrayList<>());
        prog2.lisaLause(lause('A', tehe(tehe(tehe(var('A'), var('B'), '+'), var('C'), '-'), lit(), '&'),
                ting(lit('a'), tehe(tehe(var('B'), var('C'), '&'), lit('a', 'b'), '&'))));

        HulkProgramm prog2expected = prog(new ArrayList<>());
        prog2expected.lisaLause(lause('A', lit(),
                ting(lit('a'), tehe(tehe(var('B'), var('C'), '&'), lit('a', 'b'), '&'))));

        Assert.assertEquals(prog2expected, HulkAnalyzer.processEmptyLiterals(prog2));



    }

    @Test
    public void test08_processMinus() {
        // A := (B-{}) | (({a}-{})+C) subset ((A&B)-{})
        HulkProgramm prog1 = prog(new ArrayList<>());
        prog1.lisaLause(lause('A', tehe(var('B'), lit(), '-'),
                ting(tehe(tehe(lit('a'), lit(), '-'), var('C'), '+'),
                        tehe(tehe(var('A'), var('B'), '&'), lit(), '-'))));

        HulkProgramm prog1expected = prog(new ArrayList<>());
        prog1expected.lisaLause(lause('A', var('B'),
                ting(tehe(lit('a'), var('C'), '+'), tehe(var('A'), var('B'), '&'))));
        Assert.assertEquals(prog1expected, HulkAnalyzer.processEmptyLiterals(prog1));


        // A := (({}-{a})-{}) | {a, b} subset ({}-{})
        HulkProgramm prog2 = prog(new ArrayList<>());
        prog2.lisaLause(lause('A', tehe(tehe(lit(), lit('a'), '-'), lit(), '-'),
                ting(lit('a', 'b'), tehe(lit(), lit(), '-'))));

        HulkProgramm prog2expected = prog(new ArrayList<>());
        prog2expected.lisaLause(lause('A', lit(), ting(lit('a', 'b'), lit())));
        Assert.assertEquals(prog2expected, HulkAnalyzer.processEmptyLiterals(prog2));
    }

    @Test
    public void test09_processCondition() {
        // A := ({}&{a}) | ((A&B)&{}) subset {}
        // B := {} | ({}&{a}) subset (({}&{})&{})
        HulkProgramm prog1 = prog(new ArrayList<>());
        prog1.lisaLause(lause('A', tehe(lit(), lit('a'), '&'),
                ting(tehe(tehe(var('A'), var('B'), '&'), lit(), '&'), lit())));
        prog1.lisaLause(lause('B', lit(),
                ting(tehe(lit(), lit('a'), '&'), tehe(tehe(lit(), lit(), '&'), lit(), '&'))));

        HulkProgramm prog1expected = prog(new ArrayList<>());
        prog1expected.lisaLause(lause('A', lit(), null));
        prog1expected.lisaLause(lause('B', lit(), null));
        Assert.assertEquals(prog1expected, HulkAnalyzer.processEmptyLiterals(prog1));

        // A := (((A+B)-{})-{}) | {} subset (((A+{a})-{})&B)
        // B := {} | (({}-{})-{}) subset ({}-{a, b, c})
        HulkProgramm prog2 = prog(new ArrayList<>());
        prog2.lisaLause(lause('A', tehe(tehe(tehe(var('A'), var('B'), '+'), lit(), '-'), lit(), '-'),
                ting(lit(), tehe(tehe(tehe(var('A'), lit('a'), '+'), lit(), '-'), var('B'), '&'))));
        prog2.lisaLause(lause('B', lit(),
                ting(tehe(tehe(lit(), lit(), '-'), lit(), '-'), tehe(lit(), lit('a', 'b', 'c'), '-'))));

        HulkProgramm prog2expected = prog(new ArrayList<>());
        prog2expected.lisaLause(lause('A', tehe(var('A') , var('B'), '+'), null));
        prog2expected.lisaLause(lause('B', lit(), null));
        Assert.assertEquals(prog2expected, HulkAnalyzer.processEmptyLiterals(prog2));
    }

    @Test
    public void test10_processMixed() {
        // A := (((A+B)-{})&({a}+{})) | (({}&A)-C) subset {}
        // B := ((A-{a})&(({}+B)&{}))
        // C := ((B+A)&{}) | (({a}+{})-({}&{})) subset ({a}+{b})
        HulkProgramm prog1 = prog(new ArrayList<>());
        prog1.lisaLause(lause('A', tehe(tehe(tehe(var('A'), var('B'), '+'), lit(), '-'), tehe(lit('a'), lit(), '+'), '&'),
                ting(tehe(tehe(lit(), var('A'), '&'), var('C'), '-'), lit())));
        prog1.lisaLause(lause('B', tehe(tehe(var('A'), lit('a'), '-'),
                tehe(tehe(lit(), var('B'), '+'), lit(), '&'), '&'), null));
        prog1.lisaLause(lause('C', tehe(tehe(var('B'), var('A'), '+'), lit(), '&'),
                ting(tehe(tehe(lit('a'), lit(), '+'), tehe(lit(), lit(), '&'), '-'), tehe(lit('a'), lit('b'), '+'))));

        HulkProgramm prog1expected = prog(new ArrayList<>());
        prog1expected.lisaLause(lause('A', tehe(tehe(var('A'), var('B'), '+'), lit('a'), '&'), null));
        prog1expected.lisaLause(lause('B', lit(), null));
        prog1expected.lisaLause(lause('C', lit(),
                ting(lit('a'), tehe(lit('a'), lit('b'), '+'))));

        Assert.assertEquals(prog1expected, HulkAnalyzer.processEmptyLiterals(prog1));


        // A := (A+({a}&({}-B)))
        // B := ((({a}+{})-(A&{}))+(C-({}-{a})))
        HulkProgramm prog2 = prog(new ArrayList<>());
        prog2.lisaLause(lause('A', tehe(var('A'), tehe(lit('a'), tehe(lit(), var('B'), '-'), '&'), '+'), null));
        prog2.lisaLause(lause('B', tehe(
                tehe(tehe(lit('a'), lit(), '+'), tehe(var('A'), lit(), '&'), '-'),
                tehe(var('C'), tehe(lit(), lit('a'), '-'), '-'),
                '+'
        ), null));

        HulkProgramm prog2expected = prog(new ArrayList<>());
        prog2expected.lisaLause(lause('A', var('A'), null));
        prog2expected.lisaLause(lause('B', tehe(lit('a'), var('C'), '+'), null));

        Assert.assertEquals(prog2expected, HulkAnalyzer.processEmptyLiterals(prog2));
    }


    @Test
    public void test11_compile_literaalMuutuja() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',lit(),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set());
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',lit('x'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('C',set('x'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('D',var('A'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('D',set('a'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('D',lit('x','y','z'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('D',set('x','y','z'));
        checkCompile(prog, runningEnv);
    }

    @Test
    public void test12_compile_avaldis() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(lit('x'),lit('y'),'+'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('x','y'));
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('G',tehe(var('X'),lit('b'),'-'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('G',set('a','c'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('H',tehe(var('X'),var('A'),'&'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('H',set('a'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('H',tehe(tehe(var('X'),var('A'),'&'),var('B'),'&'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('H',set());
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('H',tehe(tehe(var('X'),var('A'),'+'),var('B'),'-'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('H',set('a','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('V',tehe(tehe(var('X'),lit('a'),'-'),lit('b'),'-'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('V',set('c'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('V',tehe(var('X'),tehe(lit('a'),lit('b'),'-'),'-'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('V',set('b','c'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('B',tehe(tehe(tehe(tehe(lit('x'),lit('u','y'),'+'),var('A'),'+'),var('V'),'+'),lit('k','m'),'+'),null));
        checkCompile(prog, null);
    }

    @Test
    public void test13_compile_tingimus() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('B'),ting(var('A'),var('X'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('b'));
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('B'),ting(var('X'),var('A'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',lit('u','v'),ting(var('A'),var('A'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('u','v'));
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(tehe(var('A'),var('B'),'+'),var('C'),'&'),ting(var('A'),var('A'))));
        checkCompile(prog, null);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('X'),lit('a','y'),'-'),ting(lit('x'),lit('x','y'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('b','c'));
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',tehe(var('A'),lit('x','y'),'+'),ting(tehe(tehe(lit('x'),var('A'),'&'),var('X'),'+'),tehe(tehe(lit('a','b','c','x','y'),tehe(lit('y'),var('A'),'+'),'-'),var('A'),'+'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('C',set('a','x','y'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',tehe(var('A'),lit('x','y'),'+'),ting(tehe(tehe(lit('x'),var('A'),'&'),var('X'),'+'),tehe(lit('a','b','c','x','y'),tehe(lit('y'),var('A'),'+'),'-'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(tehe(var('A'),var('B'),'+'),var('C'),'&'),ting(tehe(var('Y'),var('X'),'+'),var('A'))));
        checkCompile(prog, null);
    }

    @Test
    public void test14_compile_laused() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',var('B'),null));
        prog.lisaLause(lause('D',var('C'),null));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a'));
        runningEnv.put('B',set('b'));
        runningEnv.put('C',set('b'));
        runningEnv.put('D',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('A'),null));
        prog.lisaLause(lause('B',var('X'),null));
        prog.lisaLause(lause('C',lit('a'),null));
        prog.lisaLause(lause('D',lit('x','y','z'),null));
        prog.lisaLause(lause('A',var('B'),ting(var('A'),var('A'))));
        prog.lisaLause(lause('A',tehe(var('A'),lit('a','y'),'-'),ting(tehe(lit('x'),var('D'),'&'),lit('x','y'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('b','c'));
        runningEnv.put('B',set('a','b','c'));
        runningEnv.put('C',set('a'));
        runningEnv.put('D',set('x','y','z'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',var('B'),null));
        prog.lisaLause(lause('D',tehe(var('C'),var('F'),'+'),null));
        checkCompile(prog, null);
    }

    @Test
    public void test15_compile_varia() {
        test01_eval_literaalMuutuja();
        test02_eval_avaldis();
        test03_eval_tingimus();
        test04_eval_laused();

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('C'),null));
        checkCompile(prog, null);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',lit('x','y'),ting(lit(),lit())));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('x','y'));
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x','y'),'+'),ting(tehe(var('A'),var('B'),'&'),tehe(tehe(var('X'),lit('a'),'&'),var('A'),'-'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X',set('a','b','c'));
        runningEnv.put('A',set('a','x','y'));
        runningEnv.put('B',set('b'));
        checkCompile(prog, runningEnv);
    }


    private void checkEval(HulkNode node, Map<Character, Set<Character>> expected) {

        Map<Character, Set<Character>> currentEnv = new HashMap<>(env);

        if (expected != null) {
            run(node, currentEnv);
            assertEquals(expected, currentEnv);
        } else {
            try {

                run(node, currentEnv);
                fail("Programm pidi viskama erindi!");
            } catch (RuntimeException ignore) {
            }
        }

    }

    private static final List<Character> SET_VARIABLES = Arrays.asList('X', 'A', 'B', 'C', 'D', 'G', 'H', 'V');

    private static int set2int(Set<Character> set) {
        List<Character> ELEM_VARIABLES = Arrays.asList('x', 'y', 'z', 'a', 'b', 'c', 'u', 'v');
        int result = 0;
        if (set != null) {
            for (int i = 0; i < ELEM_VARIABLES.size(); i++) {
                result |= CMaUtils.bool2int(set.contains(ELEM_VARIABLES.get(i))) << i;
            }
        }
        return result;
    }

    private void checkCompile(HulkNode node, Map<Character, Set<Character>> expected) {

        Map<Character, Set<Character>> currentEnv = new HashMap<>(env);

        if (expected != null) {
            CMaProgram program = HulkAnalyzer.compile(node);

            CMaStack initialStack = new CMaStack();
            for (Character variable : SET_VARIABLES) {
                initialStack.push(set2int(currentEnv.get(variable)));
            }

            CMaStack expectedStack = new CMaStack();
            for (Character variable : SET_VARIABLES) {
                expectedStack.push(set2int(expected.get(variable)));
            }

            CMaStack actualStack = CMaInterpreter.run(program, initialStack);

            assertEquals(expectedStack, actualStack);
        } else {
            // TODO: eemalda testid kus expected == null
            // saaks jätta alles, CMa simulaatoris tekiks erind LOADA, STOREA juures
        }
    }

    public static Set<Character> set(Character... elemendid) {
        return new HashSet<>(Arrays.asList(elemendid));
    }

}
