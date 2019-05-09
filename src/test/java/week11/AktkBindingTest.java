package week11;


import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;
import week9.AktkAst;
import week9.ast.AstNode;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkBindingTest {

    private AstInspector inspector;

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(500, TimeUnit.MILLISECONDS));

    @Test
    public void test01_variables() {
        inspectBinding("var x = 3;\nprint(x)");
        assertVariableBoundDeclaration("x", 0, 0);

        inspectBinding("print(x);\nvar x = 3");
        assertVariableUnbound("x", 0);

        inspectBinding("print(y)");
        assertVariableUnbound("y", 0);

        inspectBinding("var x = 3;\nprint(y)");
        assertVariableUnbound("y", 0);

        // level1_incorrect_undefined
        inspectBinding("var x = y + 1");
        assertVariableUnbound("y", 0);

        // initializer
        inspectBinding("var x = 0;\nvar y = x");
        assertVariableBoundDeclaration("x", 0, 0);
    }

    @Test
    public void test02_simpleBlocks() {
        inspectBinding("var x = 3;\n{var x = 4; print(x)}");
        assertVariableBoundDeclaration("x", 0, 1);

        inspectBinding("var x = 3;\n{var y = 4; print(x)};\nprint(x);\nprint(y)");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableUnbound("y", 0);

        inspectBinding("var x = 3;\n{var x = 4; print(x)};\nprint(x);\nprint(y)");
        assertVariableBoundDeclaration("x", 0, 1);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableUnbound("y", 0);
    }

    @Test
    public void test03_complexBlocks() {
        // level1_correct_differentBlock
        inspectBinding("var x = readInt();\n" +
                "\n" +
                "if x / 2 == 0 then {\n" +
                "    var d = 1;\n" +
                "    printInt(x + 1)\n" +
                "} else {\n" +
                "    var d = 2;\n" +
                "    printInt(x * d)\n" +
                "}");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableBoundDeclaration("x", 2, 0);
        assertVariableBoundDeclaration("d", 0, 1);

        // level1_incorrect_outsideBlock
        inspectBinding("var x = readInt();\n" +
                "\n" +
                "if x / 2 == 0 then {\n" +
                "    var y = 0;\n" +
                "    printInt(x)\n" +
                "} else {\n" +
                "    var y = 1;\n" +
                "    printInt(y)\n" +
                "};\n" +
                "\n" +
                "printInt(y)");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableBoundDeclaration("y", 0, 1);
        assertVariableUnbound("y", 1);
    }

    @Test
    public void test04_redefine() {
        // level1_correct_redefineBlock
        inspectBinding("var x = readInt();\n" +
                "\n" +
                "if x / 2 == 0 then {\n" +
                "    printInt(x)\n" +
                "} else {\n" +
                "    var x = 1;\n" +
                "    printInt(x)\n" +
                "};\n" +
                "\n" +
                "printInt(x)");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableBoundDeclaration("x", 2, 1);
        assertVariableBoundDeclaration("x", 3, 0);

        // level1_correct_redefineOrder
        inspectBinding("var x = 1;\n" +
                "\n" +
                "if x / 2 == 0 then {\n" +
                "    printInt(x); /* kuvab ekraanile 1 */\n" +
                "    var x = 2;   /* siit edasi, kuni ploki lõpuni tähendab x plokimuutujat */\n" +
                "    printInt(x)  /* kuvab ekraanile 2 */\n" +
                "} else {\n" +
                "    pass()\n" +
                "};\n" +
                "\n" +
                "printInt(x) /* kuvab ekraanile 1 */");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableBoundDeclaration("x", 2, 1);
        assertVariableBoundDeclaration("x", 3, 0);
    }

    @Test
    public void test05_parameters() {
        // level2_correct_parameter
        inspectBinding("var x = 45;\n" +
                "\n" +
                "fun printDouble(x:Integer) {\n" +
                "    print(x);\n" +
                "    print(x)\n" +
                "}");
        assertVariableBoundParameter("x", 0, 0);
        assertVariableBoundParameter("x", 1, 0);

        // level2_correct_outside
        inspectBinding("var x = 45;\n" +
                "\n" +
                "fun printDouble(y:Integer) {\n" +
                "    print(x);\n" +
                "    print(x)\n" +
                "}");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
    }

    @Test
    public void test06_multipleFunctions() {
        inspectBinding("var x = 45;\n" +
                "\n" +
                "fun pala(x:Integer) {\n" +
                "    print(x)\n" +
                "};\n" +
                "\n" +
                "fun kala() {\n" +
                "    print(x)\n" +
                "}");
        assertVariableBoundParameter("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);

        inspectBinding("var x = 45;\n" +
                "\n" +
                "fun pala(y:Integer) {\n" +
                "    print(x)\n" +
                "};\n" +
                "\n" +
                "fun kala(x:Integer) {\n" +
                "    print(x)\n" +
                "}");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundParameter("x", 1, 0);
    }

    @Test
    public void test07_functionBlocks() {
        inspectBinding("var x = 45;\n" +
                "\n" +
                "fun printDouble(y:Integer) {\n" +
                "    print(x);\n" +
                "    var x = 3;\n" +
                "    if kala > 3 then {\n" +
                "        print(x)\n" +
                "    } else { print(y)}\n" +
                "}");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableUnbound("kala", 0);
        assertVariableBoundDeclaration("x", 1, 1);
        assertVariableBoundParameter("y", 0, 0);

        inspectBinding("var x = 45;\n" +
                "\n" +
                "fun printDouble(y:Integer) {\n" +
                "    print(x);\n" +
                "    var x = 3;\n" +
                "    var y = 45;\n" +
                "    if w > e then {\n" +
                "        print(x)\n" +
                "    } else {\n" +
                "        print(y)\n" +
                "    }\n" +
                "}");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableUnbound("w", 0);
        assertVariableUnbound("e", 0);
        assertVariableBoundDeclaration("x", 1, 1);
        assertVariableBoundDeclaration("y", 0, 0);

    }

    @Test
    public void test08_assignments() {
        inspectBinding("var x = 3;\nx = 5");
        assertAssignmentBoundDeclaration("x", 0, 0);

        inspectBinding("x = 5;\nvar x = 3");
        assertAssignmentUnbound("x", 0);

        inspectBinding("y = 5");
        assertAssignmentUnbound("y", 0);

        inspectBinding("var x = 3;\ny = 5");
        assertAssignmentUnbound("y", 0);

        inspectBinding("var x = 3;\n{var x = 4; x = 5};\nx = 6");
        assertAssignmentBoundDeclaration("x", 0, 1);
        assertAssignmentBoundDeclaration("x", 1, 0);

        inspectBinding("fun kala(x:Integer) {\n" +
                "    x = 7;\n" +
                "    print(x)\n" +
                "}");
        assertAssignmentBoundParameter("x", 0, 0);
    }

    @Test
    public void test09_functionCalls() {
        inspectBinding("fun printDouble(x:Integer) {\n" +
                "    print(x);\n" +
                "    print(x)\n" +
                "};\n" +
                "\n" +
                "printDouble(3)");
        assertCallUnbound("print", 0);
        assertCallUnbound("print", 1);
        assertCallBoundDefinition("printDouble", 0, 0);
    }

    @Test
    public void test10_returns() {
        inspectBinding("fun abs(x:Integer) {\n" +
                "    if x < 0 then {\n" +
                "        return -x\n" +
                "    } else {\n" +
                "        return x\n" +
                "    }\n" +
                "};\n" +
                "\n" +
                "return 42");
        assertReturnBoundDefinition(0, "abs", 0);
        assertReturnBoundDefinition(1, "abs", 0);
        assertReturnUnbound(2);
    }

    private void inspectBinding(String program) {
        AstNode ast = AktkAst.createAst(program);
        inspector = new AstInspector(ast);
        AktkBinding.bind(ast);
    }

    // testimise abimeetodid...

    private void assertVariableBoundDeclaration(String name, int variableIndex, int declarationIndex) {
        assertSame(String.format("muutuja '%s' %d. kasutus pole seotud %d. deklaratsiooniga", name, variableIndex, declarationIndex),
                inspector.findVariableDeclaration(name, declarationIndex), inspector.findVariable(name, variableIndex).getBinding());
    }

    private void assertVariableBoundParameter(String name, int variableIndex, int parameterIndex) {
        assertSame(String.format("muutuja '%s' %d. kasutus pole seotud %d. parameetriga", name, variableIndex, parameterIndex),
                inspector.findFunctionParameter(name, parameterIndex), inspector.findVariable(name, variableIndex).getBinding());
    }

    private void assertVariableUnbound(String name, int index) {
        assertNull(String.format("muutuja '%s' %d. kasutus on seotud", name, index),
                inspector.findVariable(name, index).getBinding());
    }

    private void assertAssignmentBoundDeclaration(String variableName, int assignmentIndex, int declarationIndex) {
        assertSame(String.format("muutuja '%s' %d. omistamine pole seotud %d. deklaratsiooniga", variableName, assignmentIndex, declarationIndex),
                inspector.findVariableDeclaration(variableName, declarationIndex), inspector.findAssignment(variableName, assignmentIndex).getBinding());
    }

    private void assertAssignmentBoundParameter(String variableName, int assignmentIndex, int parameterIndex) {
        assertSame(String.format("muutuja '%s' %d. omistamine pole seotud %d. parameetriga", variableName, assignmentIndex, parameterIndex),
                inspector.findFunctionParameter(variableName, parameterIndex), inspector.findAssignment(variableName, assignmentIndex).getBinding());
    }

    private void assertAssignmentUnbound(String variableName, int index) {
        assertNull(String.format("muutuja '%s' %d. omistamine on seotud", variableName, index),
                inspector.findAssignment(variableName, index).getBinding());
    }

    private void assertCallBoundDefinition(String name, int callIndex, int definitionIndex) {
        assertSame(String.format("funktsiooni '%s' %d. kutse pole seotud %d. definitsiooniga", name, callIndex, definitionIndex),
                inspector.findFunctionDefinition(name, definitionIndex), inspector.findFunctionCall(name, callIndex).getFunctionBinding());
    }

    private void assertCallUnbound(String name, int index) {
        assertNull(String.format("funktsiooni '%s' %d. kutse on seotud", name, index),
                inspector.findFunctionCall(name, index).getFunctionBinding());
    }

    private void assertReturnBoundDefinition(int returnIndex, String functionName, int definitionIndex) {
        assertSame(String.format("%d. return pole seotud funktiooni '%s' %d. definitsiooniga", returnIndex, functionName, definitionIndex),
                inspector.findFunctionDefinition(functionName, definitionIndex), inspector.findReturn(returnIndex).getFunctionBinding());
    }

    private void assertReturnUnbound(int index) {
        assertNull(String.format("%d. return on seotud", index),
                inspector.findReturn(index).getFunctionBinding());
    }
}
