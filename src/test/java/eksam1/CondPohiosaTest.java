package eksam1;

import eksam1.ast.CondNode;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
import static eksam1.ast.CondNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CondPohiosaTest {

    @Test
    public void test01_recognize_basic() {
        legal("Arvuta: 5");
        legal("Arvuta: 0");
        illegal("Arvuta: 05");

        legal("Arvuta: jah");
        legal("Arvuta: ei");

        legal("Arvuta: x");
        legal("Arvuta: foobar");
        legal("Arvuta: true");
        illegal("Arvuta: FOO");

        illegal("Arvuta: 5 jura");
    }

    @Test
    public void test02_recognize_decls() {
        legal("x on int! Arvuta: 0");
        legal("x on bool! Arvuta: 0");

        legal("x on intike! Arvuta: 0");
        legal("x on boolakas! Arvuta: 0");
        illegal("x on in! Arvuta: 0");
        illegal("x on boo! Arvuta: 0");

        legal("x on bool! y on int! Arvuta: 0");
        legal("x on bool! y on int! z on bool! Arvuta: 0");

        illegal("x on int Arvuta: 0");
        illegal("x on int!! Arvuta: 0");
        illegal("!x on int Arvuta: 0");
        illegal("!x on int! Arvuta: 0");
        illegal("x : int! Arvuta: 0");
        illegal("xonbool! Arvuta: 0");
    }

    @Test
    public void test03_recognize_ops() {
        legal("Arvuta: -x + y - z");
        legal("Arvuta: x / -y * z");
        illegal("Arvuta: x % y");

        legal("Arvuta: a | b & !c");
        illegal("Arvuta: a && b");
        illegal("Arvuta: a || b");

        legal("Arvuta: x on y");
        illegal("Arvuta: x == y");
    }

    @Test
    public void test04_recognize_exprs() {
        legal("Arvuta: Oota 2 + 3 Valmis");
        illegal("Arvuta: (2 + 3)");
        illegal("Arvuta: Oota 2 + 3");

        legal("Arvuta: Kas a? Jah: 5 Ei: 10 Selge");

        legal("x on bool! y on int!\n" +
                "Arvuta: \n" +
                "  2 * Oota 2 + 2 Valmis -\n" +
                "  Kas 5 + 5 on 10? \n" +
                "    Jah: Kas 5 + 5 on 10? \n" +
                "           Jah: 35 * y\n" +
                "           Ei: 100 \n" +
                "         Selge \n" +
                "    Ei: 100 \n" +
                "  Selge \n" +
                "+ 30 + Kas jah & x?\n" +
                "            Jah: 10\n" +
                "            Ei: 300\n" +
                "       Selge");
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
        CondLexer lexer = new CondLexer(CharStreams.fromString(input));
        lexer.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine, String msg, RecognitionException e) {
                throw e;
            }
        });

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CondParser parser = new CondParser(tokens);

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
    public void test05_ast_basic() {
        checkAst(prog(decls(), il(5)), "Arvuta: 5");

        checkAst(prog(decls(), bl(true)), "Arvuta: jah");
        checkAst(prog(decls(), bl(false)), "Arvuta: ei");

        checkAst(prog(decls(), var("x")), "Arvuta: x");
        checkAst(prog(decls(), var("true")), "Arvuta: true");
    }

    @Test
    public void test06_ast_decls() {
        checkAst(prog(decls(iv("x")), il(0)), "x on int! Arvuta: 0");
        checkAst(prog(decls(bv("x")), il(0)), "x on bool! Arvuta: 0");

        checkAst(prog(decls(iv("x")), il(0)), "x on intike! Arvuta: 0");
        checkAst(prog(decls(bv("x")), il(0)), "x on boolakas! Arvuta: 0");

        checkAst(prog(decls(bv("x"), iv("y"), bv("z")), il(0)), "x on bool! y on int! z on bool! Arvuta: 0");
    }

    @Test
    public void test07_ast_ops() {
        checkAst(prog(decls(), sub(add(neg(var("x")), var("y")), var("z"))), "Arvuta: -x + y - z");
        checkAst(prog(decls(), mul(div(var("x"), neg(var("y"))), var("z"))), "Arvuta: x / -y * z");

        checkAst(prog(decls(), or(var("a"), and(var("b"), not(var("c"))))), "Arvuta: a | b & !c");

        checkAst(prog(decls(), eq(var("x"), var("y"))), "Arvuta: x on y");

        // assoc
        checkAst(prog(decls(), sub(sub(var("a"), var("b")), var("c"))), "Arvuta: a - b - c");

        // prio
        checkAst(prog(decls(), add(var("x"), mul(var("y"), var("z")))), "Arvuta: x + y * z");
        checkAst(prog(decls(), sub(var("a"), div(var("y"), var("z")))), "Arvuta: a - y / z");
        checkAst(prog(decls(), and(var("a"), eq(var("b"), add(var("c"), var("d"))))), "Arvuta: a & b on c + d");
        checkAst(prog(decls(), add(sub(il(1), il(2)), il(3))), "Arvuta: 1 - 2 + 3");
        checkAst(prog(decls(), sub(add(il(1), il(2)), il(3))), "Arvuta: 1 + 2 - 3");
    }

    @Test
    public void test08_ast_exprs() {
        checkAst(prog(decls(), add(il(2), il(3))), "Arvuta: Oota 2 + 3 Valmis");
        checkAst(prog(decls(), mul(add(il(2), il(3)), il(5))), "Arvuta: Oota 2 + 3 Valmis * 5");

        checkAst(prog(decls(), ifte(var("a"), il(5), il(10))), "Arvuta: Kas a? Jah: 5 Ei: 10 Selge");

        checkAst(prog(decls(bv("x"), iv("y")),
                        add(add(sub(mul(il(2), add(il(2), il(2))),
                                ifte(eq(add(il(5), il(5)), il(10)),
                                        ifte(eq(add(il(5), il(5)), il(10)),
                                                mul(il(35), var("y")),
                                                il(100)
                                        ),
                                        il(100)
                                )
                        ), il(30)), ifte(and(bl(true), var("x")),
                                il(10),
                                il(300))
                        )
                ), "x on bool! y on int!\n" +
                "Arvuta: \n" +
                "  2 * Oota 2 + 2 Valmis -\n" +
                "  Kas 5 + 5 on 10? \n" +
                "    Jah: Kas 5 + 5 on 10? \n" +
                "           Jah: 35 * y\n" +
                "           Ei: 100 \n" +
                "         Selge \n" +
                "    Ei: 100 \n" +
                "  Selge \n" +
                "+ 30 + Kas jah & x?\n" +
                "            Jah: 10\n" +
                "            Ei: 300\n" +
                "       Selge");
    }

    private void checkAst(CondNode expected, String input) {
        CondNode actual = CondPohiosa.makeCondAst(input);
        assertEquals(expected, actual);
    }
}
