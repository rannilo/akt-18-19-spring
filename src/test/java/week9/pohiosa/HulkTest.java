package week9.pohiosa;


import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week9.pohiosa.hulk.HulkAst;
import week9.pohiosa.hulk.hulkAst.HulkNode;
import week9.pohiosa.hulk.hulkAst.HulkProgramm;

import java.util.ArrayList;

import static org.junit.Assert.fail;
import static week9.pohiosa.hulk.hulkAst.HulkNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HulkTest {
    private HulkProgramm prog;

    @Test
    public void test01_recognize_literaalMuutuja() {
        legal("A := A");
        legal("B := X");
        legal("C := {a}");
        legal("D := {x, y, z}");
        legal("D := {}");

        illegal("A");
        illegal("A = A");
        illegal("A == A");
        illegal("A := x");
        illegal("x := A");
        illegal("AB := A");
        illegal("A := AB");
        illegal("A := A B");
        illegal("D := {X, Y}");
        illegal("D := {x, y,}");
    }

    @Test
    public void test02_recognize_avaldis() {
        legal("A := A + B");
        legal("C := A & B");
        legal("C := {x} - B");
        legal("B := {x} + {y, u} + A + V + {k, m}");
        legal("B := {x} - {y, u} + A & V + {k, m}");
        legal("A := ({x} - {y, u}) + (A & V + {k, m} & ({}))");

        illegal("A = A");
        illegal("A := A + x");
        illegal("A := -B");
        illegal("A + B");
        illegal("A +- B");
        illegal("A + B +");
        illegal("& A + B");
    }

    @Test
    public void test03_recognize_subsetLaused() {
        legal("A := B | A subset A");
        legal("A := A + B & C | A subset A");
        legal("A := A - {x, y} | {x} subset {x, y}");
        legal("A := A - {x, y} | {x} & A subset {x, y} - {y}");
        legal("A := A + {x, y} | ({x} & A) + X subset {x, y, z, a, b} - ({y} + A)");
        legal("A := A\n" +
                "B := X");
        legal("A := A\n" +
                "B := X\n" +
                "C := {a}\n" +
                "D := {x, y, z}\n" +
                "A := B | A subset A\n" +
                "A := A - {x, y} | {x} & A subset {x, y} - {y}");

        illegal("A := B | a subset A");
        illegal("A := B | A subset a");
        illegal("A := B  A subset A");
        illegal("A := B || A subset A");
        illegal("A := B | A subset A subset A");
        illegal("A subset A");
        illegal("A := B | A in A");
        illegal("A := B A := B");
        illegal("A := A\n");
        illegal("A := A\n" +
                "\n" +
                "B := X");

    }

    @Test
    public void test04_recognize_lisasyntaks() {
        legal("A := A + {x}");
        legal("A <- x");
        legal("A <- x, y, z, a");
        legal("A := A - {x, y, z}");
        legal("A -> x, y, z");
        legal("A := A + {x} | {x} subset C");
        legal("A <- x | x in C");
        legal("A -> x | y in C + {x}");
        legal("A := A + {x}\n" +
                "A <- x\n" +
                "A <- x, y, z, a\n" +
                "A <- x | x in C\n" +
                "A -> x | y in C + {x}");

        illegal("A -> A");
        illegal("A <- A");
        illegal("x <- A");
        illegal("x -> A");
        illegal("A := {x} | A in A");
        illegal("A := A | x in x");
        illegal("A := A | x in X in X");
        illegal("A - > x");
        illegal("A < - x");
    }

    @Test
    public void test05_recognize_varia() {
        test01_recognize_literaalMuutuja();
        test02_recognize_avaldis();
        test03_recognize_subsetLaused();
        test04_recognize_lisasyntaks();

        legal("A := B\n" +
                "A := {x, y, z} + A\n" +
                "B := A & (B - C) & {}\n" +
                "A := A + {x, y} | x in A + B\n" +
                "A <- x | {x} + {y} subset A\n" +
                "A -> a, b, c");
        legal("B   := X\n" +
                "   C := {a}\n" +
                "D := {x, y, z}\n" +
                "A   := B | A subset A\n" +
                "A := A  - { x, y}  | {x} & A subset {x, y} - {y}\n" +
                " A   := A + {x}\n" +
                "A <- x\n" +
                "A   <- x, y, z, a\n" +
                "A <- x |    x   in C\n" +
                "A   -> x | y in C  +    { x}");
    }

    @Test
    public void test06_ast_literaalMuutuja() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('A'),null));
        checkAstEquality("A := A", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('B',var('X'),null));
        checkAstEquality("B := X", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',lit('a'),null));
        checkAstEquality("C := {a}", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('D',lit('x','y','z'),null));
        checkAstEquality("D := {x, y, z}", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('D',lit(),null));
        checkAstEquality("D := {}", prog);
    }

    @Test
    public void test07_ast_avaldis() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),var('B'),'+'),null));
        checkAstEquality("A := A + B", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',tehe(var('A'),var('B'),'&'),null));
        checkAstEquality("C := A & B", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('C',tehe(lit('x'),var('B'),'-'),null));
        checkAstEquality("C := {x} - B", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('B',tehe(tehe(tehe(tehe(lit('x'),lit('u','y'),'+'),var('A'),'+'),var('V'),'+'),lit('k','m'),'+'),null));
        checkAstEquality("B := {x} + {y, u} + A + V + {k, m}", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('B',tehe(tehe(tehe(tehe(lit('x'),lit('u','y'),'-'),var('A'),'+'),var('V'),'&'),lit('k','m'),'+'),null));
        checkAstEquality("B := {x} - {y, u} + A & V + {k, m}", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(tehe(lit('x'),lit('u','y'),'-'),tehe(tehe(tehe(var('A'),var('V'),'&'),lit('k','m'),'+'),lit(),'&'),'+'),null));
        checkAstEquality("A := ({x} - {y, u}) + (A & V + {k, m} & ({}))", prog);
    }

    @Test
    public void test08_ast_subsetLaused() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('B'),ting(var('A'),var('A'))));
        checkAstEquality("A := B | A subset A", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(tehe(var('A'),var('B'),'+'),var('C'),'&'),ting(var('A'),var('A'))));
        checkAstEquality("A := A + B & C | A subset A", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x','y'),'-'),ting(lit('x'),lit('x','y'))));
        checkAstEquality("A := A - {x, y} | {x} subset {x, y}", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x','y'),'-'),ting(tehe(lit('x'),var('A'),'&'),tehe(lit('x','y'),lit('y'),'-'))));
        checkAstEquality("A := A - {x, y} | {x} & A subset {x, y} - {y}", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x','y'),'+'),ting(tehe(tehe(lit('x'),var('A'),'&'),var('X'),'+'),tehe(lit('a','b','x','y','z'),tehe(lit('y'),var('A'),'+'),'-'))));
        checkAstEquality("A := A + {x, y} | ({x} & A) + X subset {x, y, z, a, b} - ({y} + A)", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('A'),null));
        prog.lisaLause(lause('B',var('X'),null));
        checkAstEquality(
                "A := A\n" +
                "B := X", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('A'),null));
        prog.lisaLause(lause('B',var('X'),null));
        prog.lisaLause(lause('C',lit('a'),null));
        prog.lisaLause(lause('D',lit('x','y','z'),null));
        prog.lisaLause(lause('A',var('B'),ting(var('A'),var('A'))));
        prog.lisaLause(lause('A',tehe(var('A'),lit('x','y'),'-'),ting(tehe(lit('x'),var('A'),'&'),tehe(lit('x','y'),lit('y'),'-'))));
        checkAstEquality(
                "A := A\n" +
                "B := X\n" +
                "C := {a}\n" +
                "D := {x, y, z}\n" +
                "A := B | A subset A\n" +
                "A := A - {x, y} | {x} & A subset {x, y} - {y}", prog);
    }

    @Test
    public void test09_ast_lisasyntaks() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'+'),null));
        checkAstEquality("A := A + {x}", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'+'),null));
        checkAstEquality("A <- x", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('a','x','y','z'),'+'),null));
        checkAstEquality("A <- x, y, z, a", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x','y','z'),'-'),null));
        checkAstEquality("A := A - {x, y, z}", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x','y','z'),'-'),null));
        checkAstEquality("A -> x, y, z", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'+'),ting(lit('x'),var('C'))));
        checkAstEquality("A := A + {x} | {x} subset C", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'+'),ting(lit('x'),var('C'))));
        checkAstEquality("A <- x | x in C", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'-'),ting(lit('y'),tehe(var('C'),lit('x'),'+'))));
        checkAstEquality("A -> x | y in C + {x}", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'+'),null));
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'+'),null));
        prog.lisaLause(lause('A',tehe(var('A'),lit('a','x','y','z'),'+'),null));
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'+'),ting(lit('x'),var('C'))));
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'-'),ting(lit('y'),tehe(var('C'),lit('x'),'+'))));
        checkAstEquality(
                "A := A + {x}\n" +
                "A <- x\n" +
                "A <- x, y, z, a\n" +
                "A <- x | x in C\n" +
                "A -> x | y in C + {x}", prog);
    }

    @Test
    public void test10_ast_varia() {
        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('A',var('B'),null));
        prog.lisaLause(lause('A',tehe(lit('x','y','z'),var('A'),'+'),null));
        prog.lisaLause(lause('B',tehe(tehe(var('A'),tehe(var('B'),var('C'),'-'),'&'),lit(),'&'),null));
        prog.lisaLause(lause('A',tehe(var('A'),lit('x','y'),'+'),ting(lit('x'),tehe(var('A'),var('B'),'+'))));
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'+'),ting(tehe(lit('x'),lit('y'),'+'),var('A'))));
        prog.lisaLause(lause('A',tehe(var('A'),lit('a','b','c'),'-'),null));
        checkAstEquality(
                "A := B\n" +
                "A := {x, y, z} + A\n" +
                "B := A & (B - C) & {}\n" +
                "A := A + {x, y} | x in A + B\n" +
                "A <- x | {x} + {y} subset A\n" +
                "A -> a, b, c", prog);

        prog = prog(new ArrayList<>());
        prog.lisaLause(lause('B',var('X'),null));
        prog.lisaLause(lause('C',lit('a'),null));
        prog.lisaLause(lause('D',lit('x','y','z'),null));
        prog.lisaLause(lause('A',var('B'),ting(var('A'),var('A'))));
        prog.lisaLause(lause('A',tehe(var('A'),lit('x','y'),'-'),ting(tehe(lit('x'),var('A'),'&'),tehe(lit('x','y'),lit('y'),'-'))));
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'+'),null));
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'+'),null));
        prog.lisaLause(lause('A',tehe(var('A'),lit('a','x','y','z'),'+'),null));
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'+'),ting(lit('x'),var('C'))));
        prog.lisaLause(lause('A',tehe(var('A'),lit('x'),'-'),ting(lit('y'),tehe(var('C'),lit('x'),'+'))));
        checkAstEquality(
                "B   := X\n" +
                "   C := {a}\n" +
                "D := {x, y, z}\n" +
                "A   := B | A subset A\n" +
                "A := A  - { x, y}  | {x} & A subset {x, y} - {y}\n" +
                " A   := A + {x}\n" +
                "A <- x\n" +
                "A   <- x, y, z, a\n" +
                "A <- x |    x   in C\n" +
                "A   -> x | y in C  +    { x}", prog);
    }

    //----------------------------//
    private void legal(String program) {
        recognize(program, true);
    }

    private void illegal(String program) {
        recognize(program, false);
    }

    private void recognize(String program, boolean legal) {
        try {
            System.err.close();
        } catch (Throwable ignored) {
        }

        boolean parses = true;
        try {
            parseWithExceptions(program);
        } catch (Throwable e) {
            parses = false;
        }

        if (legal) {
            if (!parses) Assert.fail("sinu grammatika ei aktsepteerinud seda");
        }
        else {
            if (parses) Assert.fail("sinu grammatika aktsepteeris seda");
        }
    }

    private static ParseTree parseWithExceptions(String program) {
        CharStream input = CharStreams.fromString(program);
        HulkLexer lexer = new HulkLexer(input);
        lexer.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine, String msg, RecognitionException e) {
                throw e;
            }
        });

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HulkParser parser = new HulkParser(tokens);

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

    private void checkAstEquality(String avaldis, HulkNode expectedAst) {

        HulkNode actualAst = null;
        actualAst = HulkAst.makeHulkAst(avaldis);

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
