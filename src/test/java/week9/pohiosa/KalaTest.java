package week9.pohiosa;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;
import utils.ExceptionErrorListener;
import week7.kalaparser.KalaNode;

import java.util.HashMap;

import static org.junit.Assert.*;
import static week9.pohiosa.KalaAst.makeKalaAst;

public class KalaTest {

    private final HashMap<String, Integer> env = new HashMap<>();

    @Test
    public void test_recognize() {
        acc("(kala)");
        acc("()");
        acc("(null)");
        acc("(kala, koer, x, x)");
        acc("(kala, koer, x, null, null)");
        acc("(kala, koer, (x, null), null)");
        acc("(((((())))))");
        acc("(kala, (x,y , null, (), (kala,()) ))");

        rej("kala");
        rej("kala, (null)");
        rej("(((((()))))");
        rej("(kala, (x,y , null, (), (kala,()) )");
    }

    @Test
    public void test_ast() {
        checkAst("(kala)", "(kala)");
        checkAst("()", "()");
        checkAst("(null)", "(NULL)");
        checkAst("(kala, koer, x, x)", "(kala, koer, x, x)");
        checkAst("(kala, koer, x, null, null)", "(kala, koer, x, NULL, NULL)");
        checkAst("(kala, koer, (x, null), null)", "(kala, koer, (x, NULL), NULL)");
        checkAst("(((((())))))", "(((((())))))");
        checkAst("(kala, (x,y , null, (), (kala,()) ))", "(kala, (x, y, NULL, (), (kala, ())))");
    }

    private void acc(String input) {
        assertTrue("We must accept \"" + input + '"', parse(input));
    }

    private void rej(String input) {
        assertFalse("We must reject \"" + input + '"', parse(input));
    }

    private static final ExceptionErrorListener listener = new ExceptionErrorListener();

    public static boolean parse(String input) {
        KalaLexer lexer = new KalaLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KalaParser parser = new KalaParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(listener);
        try {
            parser.init();
            return tokens.LA(1) == -1;
        } catch (Exception e) {
            return false;
        }
    }

    private void checkAst(String input, String expected) {
        KalaNode ast = makeKalaAst(input);
        assertEquals(ast.toString(), expected);
    }
}
