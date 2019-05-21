package week11.loviosa;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import cma.CMaUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week9.pohiosa.loogika.LoogikaAst;
import week9.pohiosa.loogika.loogikaAst.LoogikaNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static week11.loviosa.LoogikaAnalyzer.eval;
import static week9.pohiosa.loogika.loogikaAst.LoogikaNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoogikaAnalyzerTest {
    public static String lastTestDescription=null;
    public static String successMessage=null;
    private Map<String, Boolean> env = new HashMap<>();

    public Map<String, Boolean> getEnv() {
        return env;
    }

    private static final List<String> VARIABLES = Arrays.asList("x", "y", "z", "a", "b", "c");

    @Before
    public void setUp() {
        env.put("x", false);
        env.put("y", true);
        env.put("z", true);
        env.put("a", true);
        env.put("b", false);
        env.put("c", true);
    }

    @Test
    public void test01_eval_basic() {
        checkEval(lit(false), false);  // 0
        checkEval(lit(true), true);  // 1
        checkEval(var("x"), false);  // x
        checkEval(var("y"), true);  // y
    }

    @Test
    public void test02_eval_operators() {
        checkEval(ja(lit(false), lit(true)), false);  // (0 JA 1)
        checkEval(ja(lit(true), lit(true)), true);  // (1 JA 1)
        checkEval(voi(lit(false), lit(true)), true);  // (0 VOI 1)
        checkEval(voi(lit(false), lit(false)), false);  // (0 VOI 0)
        checkEval(vordus(var("x"), lit(false)), true);  // (x = 0)
        checkEval(vordus(var("x"), var("c")), false);  // (x = c)
        checkEval(vordus(lit(false), lit(false)), true);  // (0 = 0)
    }

    @Test
    public void test03_eval_kuiSiisMuidu() {
        checkEval(kuiSiis(lit(true), lit(true), lit(false)), true);  // (KUI 1 SIIS 1 MUIDU 0)
        checkEval(kuiSiis(lit(false), lit(true), lit(false)), false);  // (KUI 0 SIIS 1 MUIDU 0)
        checkEval(kuiSiis(lit(true), var("x"), var("z")), false);  // (KUI 1 SIIS x MUIDU z)
        checkEval(kuiSiis(var("b"), voi(var("x"), var("y")), ja(var("x"), var("y"))), false);  // (KUI b SIIS (x VOI y) MUIDU (x JA y))
    }

    @Test
    public void test04_eval_all() {
        checkEval(kuiSiis(var("b"), voi(lit(false), var("y")), null), true);  // (KUI b SIIS (0 VOI y))
        checkEval(kuiSiis(var("b"), voi(var("x"), var("y")), ja(var("x"), lit(true))), false);  // (KUI b SIIS (x VOI y) MUIDU (x JA 1))
        checkEval(kuiSiis(var("b"), voi(var("x"), var("y")), null), true);  // (KUI b SIIS (x VOI y))
        checkEval(kuiSiis(var("b"), voi(var("x"), var("y")), ja(var("x"), var("y"))), false);  // (KUI b SIIS (x VOI y) MUIDU (x JA y))
        checkEval(kuiSiis(ja(var("b"), vordus(lit(false), lit(true))), voi(var("x"), var("y")), vordus(var("x"), var("y"))), false);  // (KUI (b JA (0 = 1)) SIIS (x VOI y) MUIDU (x = y))
        checkEval(voi(voi(ja(var("x"), var("y")), ja(var("a"), var("z"))), ja(var("b"), var("c"))), true);  // (((x JA y) VOI (a JA z)) VOI (b JA c))
        checkEval(voi(voi(ja(var("x"), var("y")), vordus(var("a"), var("z"))), ja(var("b"), var("c"))), true);  // (((x JA y) VOI (a = z)) VOI (b JA c))
    }

    @Test
    public void test05_eval_kuiSiisMuidu_nested() {
        checkEval(kuiSiis(var("a"), kuiSiis(var("a"), lit(true), lit(false)), lit(false)), true);
        checkEval(kuiSiis(var("a"), kuiSiis(var("b"), lit(false), lit(true)), lit(false)), true);
        checkEval(kuiSiis(var("b"), kuiSiis(var("b"), lit(false), lit(false)), lit(true)), true);
    }


    @Test
    public void test06_compile_basic() {
        checkCompile(lit(false), false);  // 0
        checkCompile(lit(true), true);  // 1
        checkCompile(var("x"), false);  // x
        checkCompile(var("y"), true);  // y
    }

    @Test
    public void test07_compile_operators() {
        checkCompile(ja(lit(false), lit(true)), false);  // (0 JA 1)
        checkCompile(ja(lit(true), lit(true)), true);  // (1 JA 1)
        checkCompile(voi(lit(false), lit(true)), true);  // (0 VOI 1)
        checkCompile(voi(lit(false), lit(false)), false);  // (0 VOI 0)
        checkCompile(vordus(var("x"), lit(false)), true);  // (x = 0)
        checkCompile(vordus(var("x"), var("c")), false);  // (x = c)
        checkCompile(vordus(lit(false), lit(false)), true);  // (0 = 0)
    }

    @Test
    public void test08_compile_kuiSiisMuidu() {
        checkCompile(kuiSiis(lit(true), lit(true), lit(false)), true);  // (KUI 1 SIIS 1 MUIDU 0)
        checkCompile(kuiSiis(lit(false), lit(true), lit(false)), false);  // (KUI 0 SIIS 1 MUIDU 0)
        checkCompile(kuiSiis(lit(true), var("x"), var("z")), false);  // (KUI 1 SIIS x MUIDU z)
        checkCompile(kuiSiis(var("b"), voi(var("x"), var("y")), ja(var("x"), var("y"))), false);  // (KUI b SIIS (x VOI y) MUIDU (x JA y))
    }

    @Test
    public void test09_compile_all() {
        checkCompile(kuiSiis(var("b"), voi(lit(false), var("y")), null), true);  // (KUI b SIIS (0 VOI y))
        checkCompile(kuiSiis(var("b"), voi(var("x"), var("y")), ja(var("x"), lit(true))), false);  // (KUI b SIIS (x VOI y) MUIDU (x JA 1))
        checkCompile(kuiSiis(var("b"), voi(var("x"), var("y")), null), true);  // (KUI b SIIS (x VOI y))
        checkCompile(kuiSiis(var("b"), voi(var("x"), var("y")), ja(var("x"), var("y"))), false);  // (KUI b SIIS (x VOI y) MUIDU (x JA y))
        checkCompile(kuiSiis(ja(var("b"), vordus(lit(false), lit(true))), voi(var("x"), var("y")), vordus(var("x"), var("y"))), false);  // (KUI (b JA (0 = 1)) SIIS (x VOI y) MUIDU (x = y))
        checkCompile(voi(voi(ja(var("x"), var("y")), ja(var("a"), var("z"))), ja(var("b"), var("c"))), true);  // (((x JA y) VOI (a JA z)) VOI (b JA c))
        checkCompile(voi(voi(ja(var("x"), var("y")), vordus(var("a"), var("z"))), ja(var("b"), var("c"))), true);  // (((x JA y) VOI (a = z)) VOI (b JA c))
    }

    @Test
    public void test10_compile_kuiSiisMuidu_nested() {
        checkCompile(kuiSiis(var("a"), kuiSiis(var("a"), lit(true), lit(false)), lit(false)), true);
        checkCompile(kuiSiis(var("a"), kuiSiis(var("b"), lit(false), lit(true)), lit(false)), true);
        checkCompile(kuiSiis(var("b"), kuiSiis(var("b"), lit(false), lit(false)), lit(true)), true);
    }

    private void checkCompile(LoogikaNode node, boolean expected) {
        lastTestDescription = "Väärtustan avaldist:\n\n>"
                + node.toString();

        CMaProgram program = LoogikaAnalyzer.compile(node);

        CMaStack initialStack = new CMaStack();
        for (String variable : VARIABLES) {
            initialStack.push(CMaUtils.bool2int(env.get(variable)));
        }

        CMaStack expectedStack = new CMaStack(initialStack);
        expectedStack.push(CMaUtils.bool2int(expected));

        CMaStack actualStack = CMaInterpreter.run(program, initialStack);

        assertEquals(expectedStack, actualStack);
    }





    private void checkAstEquality(String avaldis, LoogikaNode expectedAst) {
        lastTestDescription = "Annan MakeDblAst argumendiks sellise avaldise:\n\n>"
                + avaldis.replaceAll("\n", "\n>");

        successMessage = "";

        LoogikaNode actualAst;
        actualAst = LoogikaAst.makeLoogikaAst(avaldis);

        if (actualAst == null) {
            fail("sinu meetod tagastas null-i");
        } else {
            if (!actualAst.equals(expectedAst)) {
                fail("oodatav tulemus oli " + expectedAst
                        + ".\n\nSinu funktsioon andis tulemuseks " + actualAst);
            }
            else {
                successMessage = "sinu meetod andis oodatud tulemuse";
            }
        }
    }

    private void checkEval(LoogikaNode node, boolean expected) {
        lastTestDescription = "Väärtustan avaldist:\n\n>"
                + node.toString();
        assertEquals(expected, eval(node, env));
    }


}
