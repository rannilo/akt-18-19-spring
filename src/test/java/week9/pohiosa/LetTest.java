package week9.pohiosa;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week9.pohiosa.letex.LetAst;
import week9.pohiosa.letex.letAst.LetAvaldis;

import static org.junit.Assert.fail;
import static week9.pohiosa.letex.letAst.LetAvaldis.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LetTest {

    @Test
    public void test01_recognize_literaalMuutuja() {
        legal("0");
        legal("97519");
        legal("a");
        legal("A");
        legal("aBxxU");

        illegal("");
        illegal("-1");
        illegal("+1");
        illegal("1 2 3");
        illegal("a77");
        illegal("8h");
        illegal("in");
        illegal("let");
        illegal("sum");
    }

    @Test
    public void test02_recognize_suludVahe() {
        legal("1-1");
        legal("82-aG");
        legal("(a)");
        legal("(((((((a)))))))");
        legal("(1-x)-(c-55)");

        illegal("-1-a");
        illegal("()");
        illegal("-(10)");
        illegal("((((a)))");
        illegal("((a))))");
        illegal("((a))-4)-(5)");
        illegal("33--x");
        illegal("1+x");
    }

    @Test
    public void test03_recognize_lihtneLetSum() {
        legal("let x=5 in x");
        legal("let x=(10-3) in x-100");
        legal("sum i=1 to 4 in i");
        legal("sum i=1 to (10-x) in i-x");
        legal("  let x  = 11    in\n" +
                "10  -   r  ");

        illegal("let x in x");
        illegal("let x=100");
        illegal("let 100 in 10");
        illegal("let y=19 in");
        illegal("sum x in x");
        illegal("sum x=1 to 5");
        illegal("sum x=-1 to 10 in x");
        illegal("sum x=1 in x");
        illegal("sum = 1 to 10");
    }

    @Test
    public void test04_recognize_assocPrio() {
        legal("let x = 1 - x in 11");
        legal(  "let x = 44 in\n" +
                "let y = 2 in\n" +
                "let z = 0 in\n" +
                "x - y");
        legal(  "let a =\n" +
                "    let b =\n" +
                "        let c = 11 in c\n" +
                "    in b - 10\n" +
                "in let b = 10\n" +
                "in a - b");
        legal("let x =\n" +
                "    sum i = 1 to 4 in i\n" +
                "in x");
        legal("let a = 1 in \n" +
                "    sum i = 5 to y in a - i");
        legal("sum i = \n" +
                "    sum j = 1 to 100 in 0 \n" +
                "    to 5 in i");
        legal("10 - x - let y = 10 in 100 - 9");

        illegal("let x = 10 let y = 11 in x - y");
    }

    @Test
    public void test05_recognize_varia() {
        test01_recognize_literaalMuutuja();
        test02_recognize_suludVahe();
        test03_recognize_lihtneLetSum();
        test04_recognize_assocPrio();

        legal("let x = 666 in (sum i = 0 to 3; j = 0 to i in i-j) - 1");
        legal("sum \n" +
                "    x = let y = 1 in y to 11;\n" +
                "    y = 11 to 0;\n" +
                "    z = x to y\n" +
                "in\n" +
                "    let \n" +
                "        a = 11;\n" +
                "        b = 12\n" +
                "    in x-a-b");
    }

    @Test
    public void test06_ast_literaalMuutuja() {
        checkAstEquality("0", num(0));
        checkAstEquality("97519", num(97519));
        checkAstEquality("a", var("a"));
        checkAstEquality("A", var("A"));
        checkAstEquality("aBxxU", var("aBxxU"));
    }

    @Test
    public void test07_ast_suludVahe() {
        checkAstEquality("1-1", vahe(num(1),num(1)));
        checkAstEquality("82-aG", vahe(num(82),var("aG")));
        checkAstEquality("(a)", var("a"));
        checkAstEquality("(((((((a)))))))", var("a"));
        checkAstEquality("(1-x)-(c-55)", vahe(vahe(num(1),var("x")),vahe(var("c"),num(55))));
    }

    @Test
    public void test08_ast_lihtneLetSum() {
        checkAstEquality("let x=5 in x", let("x",num(5),var("x")));
        checkAstEquality("let x=(10-3) in x-100", let("x",vahe(num(10),num(3)),vahe(var("x"),num(100))));
        checkAstEquality("sum i=1 to 4 in i", sum("i",num(1),num(4),var("i")));
        checkAstEquality("sum i=1 to (10-x) in i-x", sum("i",num(1),vahe(num(10),var("x")),vahe(var("i"),var("x"))));
        checkAstEquality("  let x  = 11    in\n" +
                         "10  -   r  ",
                let("x",num(11),vahe(num(10),var("r"))));
    }

    @Test
    public void test09_ast_assocPrio() {
        checkAstEquality("let x = 1 - x in 11", let("x",vahe(num(1),var("x")),num(11)));
        checkAstEquality("let x = 44 in\n" +
                "let y = 2 in\n" +
                "let z = 0 in\n" +
                "x - y",
                let("x",num(44),let("y",num(2),let("z",num(0),vahe(var("x"),var("y"))))));
        checkAstEquality("let a =\n" +
                "    let b =\n" +
                "        let c = 11 in c\n" +
                "    in b - 10\n" +
                "in let b = 10\n" +
                "in a - b",
                let("a",let("b",let("c",num(11),var("c")),vahe(var("b"),num(10))),let("b",num(10),vahe(var("a"),var("b")))));
        checkAstEquality("let x =\n" +
                "    sum i = 1 to 4 in i\n" +
                "in x",
                let("x",sum("i",num(1),num(4),var("i")),var("x")));
        checkAstEquality("let a = 1 in \n" +
                "    sum i = 5 to y in a - i", let("a",num(1),sum("i",num(5),var("y"),vahe(var("a"),var("i")))));
        checkAstEquality("sum i = \n" +
                "    sum j = 1 to 100 in 0 \n" +
                "    to 5 in i",
                sum("i",sum("j",num(1),num(100),num(0)),num(5),var("i")));
        checkAstEquality("10 - x - let y = 10 in 100 - 9",
                vahe(vahe(num(10),var("x")),let("y",num(10),vahe(num(100),num(9)))));
    }

    @Test
    public void test10_ast_varia() {
        test06_ast_literaalMuutuja();
        test07_ast_suludVahe();
        test08_ast_lihtneLetSum();
        test09_ast_assocPrio();

        checkAstEquality("let x = 666 in (sum i = 0 to 3; j = 0 to i in i-j) - 1",
                let("x",num(666),vahe(sum("i",num(0),num(3),sum("j",num(0),var("i"),vahe(var("i"),var("j")))),num(1))));
        checkAstEquality("sum \n" +
                "    x = let y = 1 in y to 11;\n" +
                "    y = 11 to 0;\n" +
                "    z = x to y\n" +
                "in\n" +
                "    let \n" +
                "        a = 11;\n" +
                "        b = 12\n" +
                "    in x-a-b",
                sum("x",let("y",num(1),var("y")),num(11),sum("y",num(11),num(0),sum("z",var("x"),var("y"),let("a",num(11),let("b",num(12),vahe(vahe(var("x"),var("a")),var("b"))))))));
    }

    //----------------------------//
    private void legal(String program) {
        recognize(program, true);
    }

    private void illegal(String program) {
        recognize(program, false);
    }

    private void recognize(String input, boolean legal) {
        try {
            System.err.close();
        } catch (Throwable ignored) {
        }

        boolean parses = true;
        try {
            parseWithExceptions(input);
        } catch (Throwable e) {
            parses = false;
        }

        if (legal) {
            if (!parses) {
                Assert.fail("sinu grammatika ei aktsepteerinud seda");
            }
        }
        else {
            if (parses) {
                Assert.fail("sinu grammatika aktsepteeris seda");
            }
        }
    }

    private static ParseTree parseWithExceptions(String input) {
        LetexLexer lexer = new LetexLexer(CharStreams.fromString(input));
        lexer.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine, String msg, RecognitionException e) {
                throw e;
            }
        });

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LetexParser parser = new LetexParser(tokens);

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

    private void checkAstEquality(String sisend, LetAvaldis expectedAst) {

        LetAvaldis actualAst = null;
        actualAst = LetAst.fabritseeriAvaldisAst(sisend);

        if (actualAst == null) {
            fail("sinu meetod tagastas null-i");
        } else {
            if (!actualAst.equals(expectedAst)) {
                fail("oodatav tulemus oli \n" + expectedAst
                        + ".\n\nSinu funktsioon andis tulemuseks \n" + actualAst);
            }
        }
    }

}
