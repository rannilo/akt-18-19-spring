package week8.eksam;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.ExceptionErrorListener;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OldieGrammarTest {
    public static String lastTestDescription = null;
    public static String successMessage = null;


    @Test
    public void test01() {
        legal("1 END\n");
        legal("33 END\n");
        legal("898002 END\n");
        illegal("END\n");
        illegal("kala");
    }

    @Test
    public void test02() {
        // ühe omistamisega programmid
        legal("10 X = 123\n");
        legal("10 KALA$ = \"kala\"\n");
        illegal("10 KALA$ = \"kala\""); // reavahetus on rea lõpust puudu
        illegal("kala");
    }

    @Test
    public void test03() {
        // ühe print-lausega või INPUT-lausega programmid
        legal("10 PRINT 123\n");
        legal("10 PRINT \"kala\"\n");
        legal("10 INPUT \"sisesta midagi \", ST$\n");
        illegal("10 PRINT \"kala\""); // reavahetus on rea lõpust puudu
        illegal("kala");
    }

    @Test
    public void test04() {
        // binaarsed operaatorid
        legal("10 PRINT 123 + N\n");
        legal("10 PRINT \"kala\" + \"mees\"\n");
        illegal("10 PRINT \"kala\" + \n");
        illegal("kala");
    }

    @Test
    public void test05() {
        // funktsiooni väljakutsed
        legal("10 PRINT SIN(1) \n");
        legal("10 PRINT NEG(X)\n");
        illegal("10 PRINT sin(1) \n"); // väikeste tähtedega fun nimi
        illegal("10 PRINT NEG X)\n"); // alustav sulg puudub
        illegal("10 PRINT (NEG X)\n"); // väljakutse sulud puudu
        illegal("kala");
        // kombineeritud avaldised
        legal("10 PRINT SIN(1+X) \n");
        legal("10 PRINT NEG(X) + 1\n");
        legal("10 PRINT (NEG(X) + 1) * 6\n");
        legal("10 PRINT ((NEG(X) + 1) * 6)\n");
        legal("10 PRINT ((NEG(X) + 1) > 6) OR (3 >= 2)\n");
        illegal("10 PRINT SIN(1) SIN(0) \n"); // operaator puudu
        illegal("10 PRINT SIN(1) + SIN(0) XOR 45 \n"); // tundmatu operaator
        illegal("kala");
    }

    @Test
    public void test06() {
        // INPUT
        legal("10 INPUT \"sisesta midagi \", MIDAGI$\n");
        legal("10 PRINT \"tekst\"; MIDAGI$; X\n");
        illegal("10 INPUT \"sisesta midagi , MIDAGI$"); // lõpetav jutumärk puudu
        illegal("10 PRINT \"tekst\"; MIDAGI$;"); // üleliigne semikoolon
        // LAUSED
        legal("10 X = 0\n"
                + "20 IF X == 0 THEN GOTO 10\n");
        legal("10 X = 0\n"
                + "20 IF X == 0 THEN X = 10\n");
        illegal("10 X = 0\n"
                + "20 IF X == 0 THEN\n"); // THEN osa tühi
        illegal("10 X = 0\n"
                + "20 IF  THEN X = 10\n"); // TINGIMUS PUUDU

        legal("10 X = 0345\n");
        legal("10 X = 0\n");
        legal("10 X = 0\n"
                + "20 IF X == 0 THEN GOTO 10\n");
        illegal("10 X = 0\n"
                + "20 IF X == 0 THEN IF X > 0 THEN GOTO 10\n"); // IF-is võib olla vaid lihtlause
        legal("10 INPUT \"What is your name \", U$\n" +
                "20 PRINT \"Hello \"; U$\n" +
                "30 INPUT \"How many stars do you want \", N\n" +
                "40 S$ = \"\"\n" +
                "50 I = 0\n" +
                "60 S$ = S$ + \"s\"\n" +
                "65 I = I + 1\n" +
                "70 IF I < N THEN GOTO 60\n" +
                "80 PRINT S$\n" +
                "90 INPUT \"Do you want more stars \", A$\n" +
                "100 IF LEN(A$) == 0 THEN GOTO 90\n" +
                "120 IF (A$ == \"Y\") OR (A$ == \"y\") THEN GOTO 30\n" +
                "130 PRINT \"Goodbye \"; U$\n" +
                "140 END\n");

        illegal("10 INPUT \"What is your name \", U$\n" +
                "20 PRINT \"Hello \"; U$\n" +
                "30 INPUT \"How many stars do you want \", N\n" +
                "40 S$ = \"\"\n" +
                "50 I = 0\n" +
                "60 S$ = S$ + \"s\"\n" +
                "65 I = I + 1\n" +
                "70 IF I < N THEN GOTO 60\n" +
                "80 PRINT S$\n" +
                "90 INPUT \"Do you want more stars \", A$\n" +
                "100 IF LEN(A$ == 0 THEN GOTO 90\n" + // lõpetav sulg puudu
                "120 IF (A$ == \"Y\") OR (A$ == \"y\") THEN GOTO 30\n" +
                "130 PRINT \"Goodbye \"; U$\n" +
                "140 END\n");

        illegal("10 INPUT \"What is your name \", U$\n" +
                "20 PRINT \"Hello \"; U$\n" +
                "30 INPUT \"How many stars do you want \", N\n" +
                "40 S$ = \"\"\n" +
                "50 I = 0\n" +
                "60 S$ = S$ + \"s\"\n" +
                "65 I = I + 1\n" +
                "70 IF I < N THEN GOTO 60\n" +
                "80 PRINT S$\n" +
                "90 INPUT \"Do you want more stars \", A$\n" +
                "100 IF LEN(A$) == 0 THEN GOTO 90\n" +
                "120 IF (A$ == \"Y\") OR  THEN GOTO 30\n" + // OR-i parem argument puudu
                "130 PRINT \"Goodbye \"; U$\n" +
                "140 END\n");

    }


    private void check(String program, boolean legal) {
        try {
            System.err.close();
        } catch (Throwable t) {

        }

        boolean parses = true;
        try {
            parseWithExceptions(program);
        } catch (Throwable e) {
            parses = false;
        }

        lastTestDescription = "Katsetan sellise "
                + (legal ? "legaalse" : "mittelegaalse")
                + " programmiga:\n\n>"
                + program.replaceAll("\\r\\n", "\n").replaceAll("\n", "\n>");


        successMessage = "";

        if (legal) {
            if (!parses) {
                Assert.fail("sinu grammatika ei aktsepteerinud seda");
            } else {
                successMessage = "sinu grammatika aktsepteeris seda";
            }
        } else {
            if (parses) {
                Assert.fail("sinu grammatika aktsepteeris seda");
            } else {
                successMessage = "sinu grammatika ei aktsepteerinud seda";
            }
        }
    }

    private void legal(String program) {
        check(program, true);
    }

    private void illegal(String program) {
        check(program, false);
    }

    private static final ExceptionErrorListener listener = new ExceptionErrorListener();

    private static ParseTree parseWithExceptions(String program) {
        CharStream input = CharStreams.fromString(program);
        OldieLexer lexer = new OldieLexer(input);
        lexer.addErrorListener(listener);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OldieParser parser = new OldieParser(tokens);

        parser.addErrorListener(listener);

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
