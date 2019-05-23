package week11.eksamdemo;

import cma.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week11.eksamdemo.choice.ChoicePohiosa;
import week11.eksamdemo.choice.choiceAst.*;

import java.util.*;

import static cma.instruction.CMaIntInstruction.Code.LOADA;
import static cma.instruction.CMaIntInstruction.Code.LOADC;
import static cma.instruction.CMaLabelInstruction.Code.JUMP;
import static cma.instruction.CMaLabelInstruction.Code.JUMPZ;
import static org.junit.Assert.*;
import static week11.eksamdemo.choice.ChoiceAlusosa.*;
import static week11.eksamdemo.choice.ChoiceLoviosaV2.codeGen;
import static week11.eksamdemo.choice.ChoiceLoviosaV2.optimize;
import static week11.eksamdemo.choice.choiceAst.ChoiceNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChoiceTestV2 {

    private Map<String, ChoiceNode> solmap = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        solmap.put("2+3",
                add(val(2), val(3)));
        solmap.put("2|3",
                choice(val(2), val(3)));
        solmap.put("(1|2) + 3 | 5",
                choice(add(choice(val(1), val(2)), val(3)), val(5)));
        solmap.put("2 + (3|4) | 5 + (1 | 2+5)",
                choice(add(val(2), choice(val(3), val(4))), add(val(5), choice(val(1), add(val(2), val(5))))));
        solmap.put("2 + (3|(7|8)) | 5 + (1 | 2+5)",
                choice(add(val(2),choice(val(3),choice(val(7),val(8)))),add(val(5),choice(val(1),add(val(2),val(5))))));
        solmap.put("(0|(1|2)) + ((0|3)|6)",
                add(choice(val(0), choice(val(1), val(2))), choice(choice(val(0), val(3)), val(6))));
    }

    private ChoiceNode ast(String input) {
        // Kui Sul töötab AST tegija, siis asenda selle meetodi keha järgmise reaga
        // return ChoicePohiosa.makeChoiceAst(input);
        if (!solmap.containsKey(input)) {
            throw new NoSuchElementException("See on fake parser siin. Ei saa sisendeid niisama muuta!");
        }
        return solmap.get(input);
    }



    @Test
    public void test01_eval() {
        checkEval(5, "2+3", 0);
        checkEval(2, "2|3", 1000);
        checkEval(3, "2|3", -4);
        checkEval(4, "(1|2) + 3 | 5", 33333);
        checkEval(5, "2 + (3|4) | 5 + (1 | 2+5)", 20);
        checkEval(6, "2 + (3|4) | 5 + (1 | 2+5)", 4398434);
    }

    private void checkEval(int expected, String input, int seed) {
        assertEquals(expected, eval(ast(input), new Random(seed)));
    }

    @Test
    public void test02_allval() {
        assertEquals(new HashSet<>(Arrays.asList(4, 5)),
                allValues(ast("(1|2) + 3 | 5")));
        assertEquals(new HashSet<>(Arrays.asList(5,6,12)),
                allValues(ast("2 + (3|4) | 5 + (1 | 2+5)")));
    }

    @Test
    public void test03_addConst() {
        assertEquals(
                choice(val(6), val(7)),
                addConst(choice(val(3), val(4)), 3));
        assertEquals(
                choice(add(val(-8),choice(val(-7),val(-6))),add(val(-5),choice(val(-9),add(val(-8),val(-5))))),
                addConst(choice(add(val(2), choice(val(3), val(4))), add(val(5), choice(val(1), add(val(2), val(5))))), -10));
    }


    @Test
    public void test04_pohiosa() {
        for (Map.Entry<String, ChoiceNode> stringAstEntry : solmap.entrySet()) {
            String input = stringAstEntry.getKey();
            ChoiceNode ast = stringAstEntry.getValue();
            assertEquals(ast, ChoicePohiosa.makeChoiceAst(input));
        }
    }


    @Test
    public void test05_codegen() {
        checkCodegen(5, "2+3");
        checkCodegen(2, "2|3", 1);
        checkCodegen(3, "2|3", 0);
        checkCodegen(5, "(1|2) + 3 | 5", 0);
        checkCodegen(4, "(1|2) + 3 | 5", 1, 1);
        checkCodegen(5, "(1|2) + 3 | 5", 1, 0);
        checkCodegen(5, "2 + (3|4) | 5 + (1 | 2+5)", 1, 1);
        checkCodegen(6, "2 + (3|4) | 5 + (1 | 2+5)", 1, 0);
        checkCodegen(12, "2 + (3|4) | 5 + (1 | 2+5)", 0, 0);
        checkCodegen(9, "2 + (3|(7|8)) | 5 + (1 | 2+5)", 1, 0, 1);
    }

    @Test
    public void test06_codegenDynamic() {
        checkCodegen(8, "(0|(1|2)) + ((0|3)|6)", 0, 0, 0, 1);
        checkCodegen(5, "(0|(1|2)) + ((0|3)|6)", 0, 0, 1, 0);
        checkCodegen(7, "(0|(1|2)) + ((0|3)|6)", 0, 1, 0);
        checkCodegen(6, "(0|(1|2)) + ((0|3)|6)", 1, 0);
        checkCodegen(3, "(0|(1|2)) + ((0|3)|6)", 1, 1, 0);
        checkCodegen(0, "(0|(1|2)) + ((0|3)|6)", 1, 1, 1);
    }

    private static CMaStack getInitialStack(Integer[] stack) {
        List<Integer> data = new ArrayList<>(Arrays.asList(stack));
        data.add(0, 0);
        return new CMaStack(data);
    }

    private void checkCodegen(int expected, String input, Integer... stack) {
        CMaStack initialStack = getInitialStack(stack);
        assertEquals(expected, CMaInterpreter.run(codeGen(ast(input)), initialStack).peek());
    }


    @Test
    public void test07_codegenOpt() {
        checkCodegenOpt(5, "2+3");
        checkCodegenOpt(2, "2|3", 1);
        checkCodegenOpt(3, "2|3", 0);
        checkCodegenOpt(5, "(1|2) + 3 | 5", 0);
        checkCodegenOpt(4, "(1|2) + 3 | 5", 1, 1);
        checkCodegenOpt(5, "(1|2) + 3 | 5", 1, 0);
        checkCodegenOpt(5, "2 + (3|4) | 5 + (1 | 2+5)", 1, 1);
        checkCodegenOpt(6, "2 + (3|4) | 5 + (1 | 2+5)", 1, 0);
        checkCodegenOpt(12, "2 + (3|4) | 5 + (1 | 2+5)", 0, 0);
        checkCodegenOpt(9, "2 + (3|(7|8)) | 5 + (1 | 2+5)", 1, 0, 1);

        checkCodegenOpt(8, "(0|(1|2)) + ((0|3)|6)", 0, 0, 0, 1);
        checkCodegenOpt(5, "(0|(1|2)) + ((0|3)|6)", 0, 0, 1, 0);
        checkCodegenOpt(7, "(0|(1|2)) + ((0|3)|6)", 0, 1, 0);
        checkCodegenOpt(6, "(0|(1|2)) + ((0|3)|6)", 1, 0);
        checkCodegenOpt(3, "(0|(1|2)) + ((0|3)|6)", 1, 1, 0);
        checkCodegenOpt(0, "(0|(1|2)) + ((0|3)|6)", 1, 1, 1);
    }

    private void checkCodegenOpt(int expected, String input, Integer... stack) {
        ChoiceNode optimized = optimize(ast(input));
        assertFalse("Should not contain addition!", containsAdd(optimized));
        CMaStack initialStack = getInitialStack(stack);
        assertEquals(expected, CMaInterpreter.run(codeGenOpt(optimized), initialStack).peek());
    }

    private static CMaProgram codeGenOpt(ChoiceNode expr) {
        CMaProgramWriter pw = new CMaProgramWriter();

        new ChoiceVisitor<Void>() {
            int choiceCount = 1; // valikud algavad indeksist 1 (mitte 0)

            @Override
            protected Void visit(ChoiceValue value) {
                pw.visit(LOADC, value.getValue());
                return null;
            }

            @Override
            protected Void visit(ChoiceAdd add) {
                throw new UnsupportedOperationException(); // optimeeritud avaldises pole liitmisi mida kompileerida
            }

            @Override
            protected Void visit(ChoiceDecision decision) {
                CMaLabel _false = new CMaLabel();
                CMaLabel _done = new CMaLabel();
                pw.visit(LOADA, choiceCount++);
                pw.visit(JUMPZ, _false);

                int oldCount = choiceCount;
                visit(decision.getTrueChoice());
                pw.visit(JUMP, _done);

                pw.visit(_false);
                choiceCount = oldCount;
                visit(decision.getFalseChoice());
                pw.visit(_done);
                return null;
            }
        }.visit(expr);

        return pw.toProgram();
    }

    private boolean containsAdd(ChoiceNode node) {
        return new ChoiceVisitor<Boolean>() {
            @Override
            protected Boolean visit(ChoiceValue value) {
                return false;
            }

            @Override
            protected Boolean visit(ChoiceAdd add) {
                return true;
            }

            @Override
            protected Boolean visit(ChoiceDecision decision) {
                return visit(decision.getTrueChoice()) || visit(decision.getFalseChoice());
            }
        }.visit(node);
    }

}
