package eksam.pohiosa;

import eksam.pohiosa.ujukomaAst.UjukomaNode;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static eksam.pohiosa.ujukomaAst.UjukomaNode.*;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UjukomaTest {
    public static String lastTestDescription=null;
    public static String successMessage=null;

    @Test
    public void test01_rec_aatom() {
        legal("0");
        legal("1");
        legal("x");
        legal("2000");
        legal("a");

        illegal("-1");
        illegal("00");
        illegal("0x");
        illegal("0 0");
        illegal("x y");
        illegal("kala");
        illegal("abcdefgrstu");
    }

    @Test
    public void test02_rec_ujukoma() {
        legal("0.0");
        legal("1.00");
        legal("132.0030");
        legal(".3232");
        legal("0.2323");

        illegal("1.");
        illegal("00.0");
        illegal("101 . 22");
    }

    @Test
    public void test03_rec_ops_int() {
        legal("1 + 1");
        legal("10+100*30");
        legal("1 + i * 3");
        legal("(1+2) * 3");

        illegal("1 + + 343");
        illegal("(1+2 * 3");
    }

    @Test
    public void test04_rec_ops_dbl() {
        legal("1.0 + 1.0");
        legal("10.0+100.0*30.33");
        legal("1.43 + a * 3.333333");
        legal("(1.0+2.0) * 3.0");

        illegal("1.0 + + 343.343");
        illegal("(1.0+2.0 * 3.0");
    }


    @Test
    public void test05_rec_types() {
        legal("1.0 + 1.0");
        legal("1 + 2");
        legal("x + y + 4");
        legal("a + b + 4.0");
        legal("33.0 / 2.0");
        legal("a/b");

        illegal("1 + 1.0");
        illegal("1.0 + 1");
        illegal("a + x");
        illegal("33/2");
        illegal("x/y");
    }

    @Test
    public void test06_ast_atoms() {
        checkAstEquality("1", lit(1));
        checkAstEquality("x", var('x'));
        checkAstEquality("a", var('a'));
        checkAstEquality("2.5", lit(2.5));
    }

    @Test
    public void test07_ast_ops() {
        checkAstEquality("x + y", op('+', var('x'), var('y')));
    }

    @Test
    public void test08_ast_assotsiatiivsus() {
        checkAstEquality("a / b / c", op('/', op('/', var('a'), var('b')), var('c')));
    }

    @Test
    public void test09_ast_prioriteet() {
        checkAstEquality("x + y * z", op('+', var('x'), op('*', var('y'), var('z'))));
    }

    @Test
    public void test10_ast_all() {
        checkAstEquality("(1.0+2.0) * 3.0",
                op('*', op('+', lit(1.0), lit(2.0)), lit(3.0)));
        checkAstEquality("(1.0+2.0) * (a / b / c)",
                op('*', op('+', lit(1.0), lit(2.0)), op('/', op('/', var('a'), var('b')), var('c'))));
    }

    private void legal(String program) {
        recognize(program, true);
    }

    private void illegal(String program) {
        recognize(program, false);
    }

    private void recognize(String avaldis, boolean legal) {
        try {
            System.err.close();
        } catch (Throwable ignored) {
        }

        boolean parses = true;
        try {
            parseWithExceptions(avaldis);
        } catch (Throwable e) {
            parses = false;
        }

        lastTestDescription = "Katsetan sellise "
                + (legal ? "legaalse" : "mittelegaalse")
                + " avaldisega:\n\n>"
                + avaldis.replaceAll("\n", "\n>");

        successMessage = "";

        if (legal) {
            if (!parses) {
                Assert.fail("sinu grammatika ei aktsepteerinud seda");
            } else {
                successMessage = "sinu grammatika aktsepteeris seda";
            }
        }
        else {
            if (parses) {
                Assert.fail("sinu grammatika aktsepteeris seda");
            } else {
                successMessage = "sinu grammatika ei aktsepteerinud seda";
            }
        }
    }

    private static ParseTree parseWithExceptions(String program) {
        CharStream input = CharStreams.fromString(program);
        UjukomaLexer lexer = new UjukomaLexer(input);
        lexer.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine, String msg, RecognitionException e) {
                throw e;
            }
        });

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        UjukomaParser parser = new UjukomaParser(tokens);

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

    private void checkAstEquality(String avaldis, UjukomaNode expectedAst) {
        lastTestDescription = "Annan MakeDblAst argumendiks sellise avaldise:\n\n>"
                + avaldis.replaceAll("\n", "\n>");

        successMessage = "";

        UjukomaNode actualAst = null;
        actualAst = UjukomaAst.MakeUjukomaAst(avaldis);

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

}
