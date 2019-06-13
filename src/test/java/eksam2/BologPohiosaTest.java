package eksam2;

import eksam2.ast.BologNode;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static eksam2.ast.BologNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BologPohiosaTest {

    public void test01_recognize_basic() {
        legal("X");
        legal("JAH");
        legal("JAH\n");
        illegal("X jura");
    }


    @Test
    public void test02_recognize_ops() {
        legal("X && P");
        legal("X || Y <> Z");
        legal("X || (X <> Z)");
        illegal("X || ||");
    }

    @Test
    public void test03_recognize_all() {
        legal("X kui P ja Q\n" +
                "X && P kui P, P||Q ja P kui Q ja R\n" +
                "!X && P kui R && JAH || EI\n");
        illegal("X kui P, Q\n" +
                "X && P kui P, P||Q ja P kui Q ja R\n" +
                "!X && P kui R && JAH || EI\n");
    }

    private void legal(String input) {
        parseWithExceptions(input);
    }

    private void illegal(String input) {
        try {
            parseWithExceptions(input);
            fail("Vigane sisend!");
        } catch (Exception e) {

        }
    }

    private static ParseTree parseWithExceptions(String input) {
        BologLexer lexer = new BologLexer(CharStreams.fromString(input));
        lexer.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine, String msg, RecognitionException e) {
                throw e;
            }
        });

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BologParser parser = new BologParser(tokens);

        parser.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine, String msg, RecognitionException e) {
                throw e;
            }
        });

        ParseTree parseTree = parser.init();
        if (parseTree == null
                || parseTree.getChildCount() == 0
                || parser.getNumberOfSyntaxErrors() != 0
        ) {
            throw new RuntimeException("Problem with parsing");
        }

        if (tokens.LA(1) != -1) {
            throw new RuntimeException("Some tokens left after parsing");
        }

        return parseTree;
    }

    @Test
    public void test04_ast_basic() {
        checkAst("X", var("X"));
        checkAst("JAH", tv(true));
        checkAst("EI", tv(false));
        checkAst("EIP", var("EIP"));
    }

    @Test
    public void test05_ast_ops() {
        checkAst("X && P", nand(tv(true), nand(var("X"), var("P"))));
        checkAst("X || Y <> Z", nand(tv(true), nand(nand(nand(tv(true), nand(nand(tv(true), var("X")), nand(tv(true), var("Y")))), nand(tv(true), var("Z"))), nand(nand(nand(tv(true), var("X")), nand(tv(true), var("Y"))), var("Z")))));
        checkAst("X || (X <> Z)", nand(nand(tv(true), var("X")), nand(tv(true), nand(tv(true), nand(nand(nand(tv(true), var("X")), nand(tv(true), var("Z"))), nand(var("X"), var("Z")))))));
    }

    @Test
    public void test06_ast_kui() {
        checkAst("X kui P", imp(var("X"), var("P")));
        checkAst("X kui P ja Q", imp(var("X"), var("P"), var("Q")));
        checkAst("X kui P, Q ja Q", imp(var("X"), var("P"), var("Q"), var("Q")));
        checkAst("X kui", imp(var("X")));
    }

    @Test
    public void test07_ast_all() {
        checkAst("X kui P ja Q\n" +
                "X && P kui P, P||Q ja P kui Q ja R\n" +
                "!X && P kui R && JAH || EI\n",
                imp(nand(tv(true), nand(var("X"), var("P"))), var("P"), nand(nand(tv(true), var("P")), nand(tv(true), var("Q"))), imp(var("P"), var("Q"), var("R"))),
                imp(var("X"), var("P"), var("Q")),
                imp(nand(tv(true), nand(nand(tv(true), var("X")), var("P"))), nand(nand(tv(true), nand(tv(true), nand(var("R"), tv(true)))), nand(tv(true), tv(false)))));
    }


    private void checkAst(String input, BologNode... asts) {
        Set<BologNode> actual = BologPohiosa.makeBologAst(input);
        Set<BologNode> expected = new HashSet<>(Arrays.asList(asts));
        assertEquals(expected, actual);
    }
}
