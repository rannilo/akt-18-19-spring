package week8;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkGrammarTest {
    public static String lastTestDescription=null;
    public static String successMessage=null;

    // NB! Kõik näidatud programmitekstid testitakse reegli 'programm' vastu.

    @Test
    public void test01_names() {
        legal("a");
        legal("abc");
        legal("AcB");
        legal("A_234_c");
        legal("A___");
        legal("Ab2");
        illegal("2Ab2");
        illegal("Ab$");
        illegal("@");
        illegal(".");
    }

    @Test
    public void test02_literals() {
        // integers
        legal("0");
        legal("1");
        legal("21");
        legal("9862342764827346572635472653482534");
        illegal("07");

        // strings
        legal("\"\"");
        legal("\" \"");
        legal("\"tere\"");
        legal("\"1234\"");
        legal("\"tere vana kere!\"");
        legal("\" -- Tere\tVana kere ,.;\\/123\"");
        illegal("'Tere vana kere'");
        illegal("\"");
    }

    @Test
    public void test03_expressions() {
        legal("3+3/4");
        legal("3+kol_pol + 4-(32-12)");
        legal("12/0/0/0");
        legal("a > b");
        legal("('*'|'/'|'%')");
        legal("a > (b > c)");
        legal("(a >= b) <= c");
        legal("(x != y) == true");
        legal("((((((((3+4))))))))");
        legal("3 + (2*(3))");
        legal("3 % 2");
        illegal("a > 8 == 1");
        illegal("x == y == z");
        illegal("3++12");
        illegal("3/12/");
        illegal("3 (+ 2)");
        illegal("3 + (2");
        illegal("3 + (2))");
        illegal("()"); // tühjad sulud on lubatud ainult funktsiooni väljakutses
        illegal("[2+3]");
    }

    @Test
    public void test04_functionCalls() {
        legal("sin(x)");
        legal("print()");
        legal("print(a+b)");
        legal("print(a+b, sin(sin(x)), 8)");
        legal("priNt_ln (     )");
        legal("sin(x) * cos(y)");
        legal("(cos(y))");
        illegal("print(123");
        illegal("print 123");
        illegal("print 123)");
    }

    @Test
    public void test05_controlStructures() {
        legal("if a > b then a else b");
        legal("if a > b then\n\ta\nelse\n\tb");
        legal("if a+34 > sin(345*pi) then\n\ta\nelse\n\tb");
        legal("if (a+34) > sin(345*pi) then\n\t(a)\nelse\n\tb");
        legal("if ((a+34) > sin(345*pi)) then\n\t(a)\nelse\n\tb");
        legal("while a > b do print(123*x)");
        legal("while a > b do\n\tprint(123*x)");
        legal("while (a > b) do print(123*x)");
        illegal("(if a > b then a else b)");
        illegal("if a > b then a; else b;");
        illegal("while a > b \n\tprint(123*x)");
    }

    @Test
    public void test06_variables() {
        legal("var x = 123");
        legal("var x = print(x)");
        legal("x = print(x) + 234 == 22");
        illegal("print(x=234)");
        illegal("(x=234)");
        illegal("if x=3 then x else y");
    }

    @Test
    public void test07_sequences() {
        legal("print(x);kala;x = 123; if true then false else true");
        legal("print(x)\n;kala;\n\tx = 123");
        legal("{x=3}");
        legal("{x=3};{y=x}");
        legal("if x==3 then {x} else {y}");
        legal("if x==3 then {var x = 45; print(x)} else asdf");
        legal("if x > y then x else y; if a > b then 1 else 0");
        legal("while x < y do {x = x + 1; y = y - 1}; print(x)");
        illegal("print(x);kala;x = 123;");
        illegal("if a > b then print(a); else print b;");
        illegal("if a > b then {print(a);} else {print b;}");
    }

    @Test
    public void test08_combination() {
        legal("/* Muutujate deklaratsioonid */\n" +
                "var palk = -990; var nimi = \"Teele\";\n" +
                " \n" +
                "/* Funktsiooni valjakutse */\n" +
                "print(nimi, palk);\n" +
                " \n" +
                "n = int(input(\"sisesta arv\"));\n" +
                " \n" +
                "if n > 100 then {\n" +
                "    print(\"norrmaalne!\")\n" +
                "} else {\n" +
                "    print(\"lahja!!\")\n" +
                "};   /* NB! Ara unusta semikoolonit lausete vahel! */\n" +
                " \n" +
                "var i = 0;\n" +
                " \n" +
                "while i < n do {\n" +
                "    if (i > (3)) then print(i) else pass;\n" +
                "    i = i + 1\n" +
                "};\n" +
                " \n" +
                "print(\"The End!\") /* viimase lause lopus pole semikoolonit */");
        illegal("/* Muutujate deklaratsioonid */\n" +
                "var palk = -990; var nimi = \"Teele\";\n" +
                "/* Funktsiooni valjakutse */ vigane kommentaar */\n" +
                "print(nimi, palk);\n");
    }

    @Test
    public void test09_types() {
        legal("var x");
        legal("var x: Integer");
        legal("var x: Suvatyyp");
        legal("var x: Integer = 1");
        legal("fun foo() {1}");
        legal("fun snd(x:Integer, y:Integer) -> Integer {return y}");
        illegal("print(x:Integer)");
        illegal("print(x):Integer");
    }

    @Test
    public void test10_pitfalls() {
        // Vead, millest ei taha palju punkte maha võtta
        legal("----c");
        legal("--(--c + 23)");
        legal("2 * -x");
        legal("2 - -x");
        legal("-sin(-x-y)");
        legal("priNt_ln (/* argumente pole, vt. ka blah() */)");
        legal("if a > b then a else if b > c then c else b");
        illegal("3 + 56 #^!");
        illegal("\"Tere\"vana kere\"");
        illegal("blah /* blah */ blah */");
        illegal("\"Tere\nvana kere\"");
        illegal("\"\nTerevana kere\"");

    }


    private void legal(String program) {
        check(program, true);
    }

    private void illegal(String program) {
        check(program, false);
    }

    private void check(String program, boolean legal) {
        try {
            System.err.close();
        } catch (Throwable t) {

        }



        boolean parses = true;

        lastTestDescription = "Katsetan sellise "
                + (legal ? "legaalse" : "mittelegaalse")
                + " programmiga:\n\n>"
                + program.replaceAll("\n", "\n>");

        try {
            parseWithExceptions(program);
        } catch (OutOfMemoryError e) {
            Assert.fail("teed siin mingit sigadust, et mälu saab mul otsa!");
        } catch (Throwable e) {
            parses = false;
        }


        successMessage = "";

        if (legal) {
            if (!parses) {
                Assert.fail("sinu grammatika ei aktsepteerinud seda");
            } else {
                successMessage = "sinu grammatika aktsepteeris seda";
            }
        }
        else if (!legal) {
            if (parses) {
                Assert.fail("sinu grammatika aktsepteeris seda");
            } else {
                successMessage = "sinu grammatika ei aktsepteerinud seda";
            }
        }
    }

    private static ParseTree parseWithExceptions(String program) {
        CharStream input = CharStreams.fromString(program);
        AktkLexer lexer = new AktkLexer(input);
        lexer.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, 
            		int charPositionInLine, String msg, RecognitionException e) {
                throw e;
            }
        });

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AktkParser parser = new AktkParser(tokens);

        parser.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, 
            		int charPositionInLine, String msg, RecognitionException e) {
                throw e;
            }
        });

        ParseTree parseTree = parser.programm();
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
}
