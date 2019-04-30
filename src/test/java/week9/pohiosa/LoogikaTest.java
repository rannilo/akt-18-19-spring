package week9.pohiosa;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week9.pohiosa.loogika.LoogikaAst;
import week9.pohiosa.loogika.loogikaAst.LoogikaNode;

import static org.junit.Assert.fail;
import static week9.pohiosa.loogika.loogikaAst.LoogikaNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoogikaTest {
    public static String lastTestDescription=null;
    public static String successMessage=null;

    @Test
    public void test01_recognize_literaalMuutuja() {
        legal("0");
        legal("1");
        legal("x");
        legal("kala");
        legal("abcdefgrstu");

        illegal("2");
        illegal("-1");
        illegal("00");
        illegal("0x");
        illegal("0 0");
        illegal("x y");
    }

    @Test
    public void test02_recognize_simpleOperators() {
        legal("0 JA 0");
        legal("0 VOI 1");
        legal("x VOI 1");
        legal("x JA kalad");
        legal("x NING kalad");
        legal("x = 1");
        legal("x = z");
        legal("x = z");
        legal("(x = z) JA (0 VOI z)");
        legal("(((x = z) JA (0 VOI z)) NING y)");

        illegal("0 JA 3");
        illegal("0 JA 00");
        illegal("0 JA");
        illegal("JA 0");
        illegal("x == y");
        illegal("x == y");
        illegal("x =(= y)");
        illegal("(((x JA 0)");
        illegal("(((x JA 0)))))");
        illegal("x JA NING y");
        illegal("x ja y");
        illegal("x ning y");
        illegal("x voi y");
    }

    @Test
    public void test03_recognize_kuiSiis() {
        legal("KUI 0 SIIS 1 MUIDU 0");
        legal("KUI x SIIS y MUIDU maja");
        legal("KUI (x = 1) SIIS (z VOI y) MUIDU maja");
        legal("KUI (x = 1) SIIS y");
        legal("KUI (x = y) SIIS (KUI a SIIS b MUIDU c)");
        legal("KUI (x = y) SIIS (KUI a SIIS b MUIDU c) MUIDU (KUI a SIIS b)");

        illegal("KUI 0 MUIDU 1");
        illegal("SIIS 0 MUIDU 1");
        illegal("KUI SIIS 0 MUIDU 1");
        illegal("KUI y SIIS MUIDU 1");
        illegal("KUI y SIIS a MUIDU");
        illegal("KUI y SIIS a MUIDU b MUIDU x");
        illegal("KUI y KUI x SIIS a MUIDU b");
        illegal("KUI y SIIS JA a MUIDU b");
        illegal("KUI y SIIS ((a) MUIDU b");
    }

    @Test
    public void test04_recognize_assotsiatiivsus() {
        legal("a JA 1 JA b JA     c      ");
        legal("a VOI 1 VOI b VOI c");
        legal("a NING 1   NING b  NING c");
        legal("a NING 1 NING b JA c VOI e VOI j NING k");
        legal("((a NING 1    NING b JA c)) VOI e VOI (j NING k)");
        legal("1 = 0   JA a =    0 VOI b =  c    NING 1 JA  x");
        legal("KUI x SIIS y MUIDU maja");
        legal("KUI x   = y SIIS KUI a SIIS b MUIDU c MUIDU KUI a SIIS b");
        legal("KUI  KUI x = y SIIS d SIIS KUI a SIIS b MUIDU c MUIDU KUI a SIIS b");

        illegal("a = b = c");
        illegal("1 = 1 = 1");
        illegal("1 JA JA 1");
        illegal("1 VOI");
        illegal("0 0");
        illegal("2");
        illegal("KUI y SIIS a MUIDU b MUIDU x");
        illegal("KUI y KUI x SIIS a MUIDU b");
        illegal("KUI y SIIS ((a) MUIDU b");
    }

    @Test
    public void test05_ast_literaalMuutuja() {
        checkAstEquality("0", lit(false));  // 0
        checkAstEquality("1", lit(true));  // 1
        checkAstEquality("x", var("x"));  // x
        checkAstEquality("kala", var("kala"));  // kala
        checkAstEquality("abcdefgrstu", var("abcdefgrstu"));  // abcdefgrstu
    }

    @Test
    public void test06_ast_simpleOperators() {
        checkAstEquality("0 JA 0", ja(lit(false), lit(false)));  // (0 JA 0)
        checkAstEquality("0 VOI 1", voi(lit(false), lit(true)));  // (0 VOI 1)
        checkAstEquality("x VOI 1", voi(var("x"), lit(true)));  // (x VOI 1)
        checkAstEquality("x JA kalad", ja(var("x"), var("kalad")));  // (x JA kalad)
        checkAstEquality("x NING kalad", ja(var("x"), var("kalad")));  // (x JA kalad)
        checkAstEquality("x = 1", vordus(var("x"), lit(true)));  // (x = 1)
        checkAstEquality("x = z", vordus(var("x"), var("z")));  // (x = z)
        checkAstEquality("x = z", vordus(var("x"), var("z")));  // (x = z)
        checkAstEquality("(x = z) JA (0 VOI z)", ja(vordus(var("x"), var("z")), voi(lit(false), var("z"))));  // ((x = z) JA (0 VOI z))
        checkAstEquality("(((x = z) JA (0 VOI z)) NING y)", ja(ja(vordus(var("x"), var("z")), voi(lit(false), var("z"))), var("y")));  // (((x = z) JA (0 VOI z)) JA y)
    }

    @Test
    public void test07_ast_kuiSiis() {
        checkAstEquality("KUI 0 SIIS 1 MUIDU 0", kuiSiis(lit(false), lit(true), lit(false)));  // (KUI 0 SIIS 1 MUIDU 0)
        checkAstEquality("KUI x SIIS y MUIDU maja", kuiSiis(var("x"), var("y"), var("maja")));  // (KUI x SIIS y MUIDU maja)
        checkAstEquality("KUI (x = 1) SIIS (z VOI y) MUIDU maja", kuiSiis(vordus(var("x"), lit(true)), voi(var("z"), var("y")), var("maja")));  // (KUI (x = 1) SIIS (z VOI y) MUIDU maja)
        checkAstEquality("KUI (x = 1) SIIS y", kuiSiis(vordus(var("x"), lit(true)), var("y"), null));  // (KUI (x = 1) SIIS y)
        checkAstEquality("KUI (x = y) SIIS (KUI a SIIS b MUIDU c)", kuiSiis(vordus(var("x"), var("y")), kuiSiis(var("a"), var("b"), var("c")), null));  // (KUI (x = y) SIIS (KUI a SIIS b MUIDU c))
        checkAstEquality("KUI (x = y) SIIS (KUI a SIIS b MUIDU c) MUIDU (KUI a SIIS b)", kuiSiis(vordus(var("x"), var("y")), kuiSiis(var("a"), var("b"), var("c")), kuiSiis(var("a"), var("b"), null)));  // (KUI (x = y) SIIS (KUI a SIIS b MUIDU c) MUIDU (KUI a SIIS b))
    }

    @Test
    public void test08_ast_assotsiatiivsus() {
        checkAstEquality("a JA 1 JA b JA     c      ", ja(ja(ja(var("a"), lit(true)), var("b")), var("c")));  // (((a JA 1) JA b) JA c)
        checkAstEquality("a VOI 1 VOI b VOI c", voi(voi(voi(var("a"), lit(true)), var("b")), var("c")));  // (((a VOI 1) VOI b) VOI c)
        checkAstEquality("a NING 1   NING b  NING c", ja(ja(ja(var("a"), lit(true)), var("b")), var("c")));  // (((a JA 1) JA b) JA c)
        checkAstEquality("((a NING 1    NING b NING c)) VOI e VOI (j NING k)", voi(voi(ja(ja(ja(var("a"), lit(true)), var("b")), var("c")), var("e")), ja(var("j"), var("k"))));  // (((((a JA 1) JA b) JA c) VOI e) VOI (j JA k))
        checkAstEquality("((1 = 0)   JA (a =    0) JA (b =  c))    VOI (1 JA  x)", voi(ja(ja(vordus(lit(true), lit(false)), vordus(var("a"), lit(false))), vordus(var("b"), var("c"))), ja(lit(true), var("x"))));  // ((((1 = 0) JA (a = 0)) JA (b = c)) VOI (1 JA x))
        checkAstEquality("KUI x SIIS y MUIDU maja", kuiSiis(var("x"), var("y"), var("maja")));  // (KUI x SIIS y MUIDU maja)
        checkAstEquality("KUI x SIIS KUI a SIIS b MUIDU c MUIDU KUI a SIIS b MUIDU e", kuiSiis(var("x"), kuiSiis(var("a"), var("b"), var("c")), kuiSiis(var("a"), var("b"), var("e"))));  // (KUI x SIIS (KUI a SIIS b MUIDU c) MUIDU (KUI a SIIS b MUIDU e))
        checkAstEquality("KUI  KUI  y SIIS d  MUIDU c SIIS KUI a SIIS b MUIDU c MUIDU a", kuiSiis(kuiSiis(var("y"), var("d"), var("c")), kuiSiis(var("a"), var("b"), var("c")), var("a")));  // (KUI (KUI y SIIS d MUIDU c) SIIS (KUI a SIIS b MUIDU c) MUIDU a)
    }

    @Test
    public void test09_ast_prioriteet() {
        checkAstEquality("a VOI b JA c", voi(var("a"), ja(var("b"), var("c"))));  // (a VOI (b JA c))
        checkAstEquality("a VOI b NING c", ja(voi(var("a"), var("b")), var("c")));  // ((a VOI b) JA c)
        checkAstEquality("a JA b NING c", ja(ja(var("a"), var("b")), var("c")));  // ((a JA b) JA c)
        checkAstEquality("a JA (b NING c)", ja(var("a"), ja(var("b"), var("c"))));  // (a JA (b JA c))
        checkAstEquality("a NING b JA c", ja(var("a"), ja(var("b"), var("c"))));  // (a JA (b JA c))
        checkAstEquality("a JA b JA c NING x VOI e", ja(ja(ja(var("a"), var("b")), var("c")), voi(var("x"), var("e"))));  // (((a JA b) JA c) JA (x VOI e))
        checkAstEquality("a JA b JA c NING x VOI e JA x JA u VOI k NING l VOI a", ja(ja(ja(ja(var("a"), var("b")), var("c")), voi(voi(var("x"), ja(ja(var("e"), var("x")), var("u"))), var("k"))), voi(var("l"), var("a"))));  // ((((a JA b) JA c) JA ((x VOI ((e JA x) JA u)) VOI k)) JA (l VOI a))
        checkAstEquality("a = b JA c = x VOI 1", voi(ja(vordus(var("a"), var("b")), vordus(var("c"), var("x"))), lit(true)));  // (((a = b) JA (c = x)) VOI 1)
        checkAstEquality("KUI 1 SIIS y MUIDU o", kuiSiis(lit(true), var("y"), var("o")));  // (KUI 1 SIIS y MUIDU o)
        checkAstEquality("KUI 1 SIIS KUI u SIIS b MUIDU o", kuiSiis(lit(true), kuiSiis(var("u"), var("b"), var("o")), null));  // (KUI 1 SIIS (KUI u SIIS b MUIDU o))
        checkAstEquality("KUI 1 SIIS (KUI u SIIS b) MUIDU o", kuiSiis(lit(true), kuiSiis(var("u"), var("b"), null), var("o")));  // (KUI 1 SIIS (KUI u SIIS b) MUIDU o)
        checkAstEquality("KUI x   = y SIIS KUI a SIIS b MUIDU c MUIDU KUI a SIIS b", kuiSiis(vordus(var("x"), var("y")), kuiSiis(var("a"), var("b"), var("c")), kuiSiis(var("a"), var("b"), null)));  // (KUI (x = y) SIIS (KUI a SIIS b MUIDU c) MUIDU (KUI a SIIS b))
        checkAstEquality("KUI (x = y) = 0 SIIS a NING b VOI x MUIDU g VOI h NING a NING f", kuiSiis(vordus(vordus(var("x"), var("y")), lit(false)), ja(var("a"), voi(var("b"), var("x"))), ja(ja(voi(var("g"), var("h")), var("a")), var("f"))));  // (KUI ((x = y) = 0) SIIS (a JA (b VOI x)) MUIDU (((g VOI h) JA a) JA f))
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
        LoogikaLexer lexer = new LoogikaLexer(input);
        lexer.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine, String msg, RecognitionException e) {
                throw e;
            }
        });

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LoogikaParser parser = new LoogikaParser(tokens);

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

    private void checkAstEquality(String avaldis, LoogikaNode expectedAst) {
        lastTestDescription = "Annan makeLoogikaAst argumendiks sellise avaldise:\n\n>"
                + avaldis.replaceAll("\n", "\n>");

        successMessage = "";

        LoogikaNode actualAst = null;
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

}
