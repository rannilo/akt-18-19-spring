package week8.demos;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;
import utils.ExceptionErrorListener;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExprGrammarTest {

    @Test
    public void test_int() {
        acc("5+5");
        rej("56++");
    }

    @Test
    public void test_ident() {
        acc("x+x*x");
    }

    private void acc(String input) {
        assertTrue("We must accept \"" + input + '"', parse(input));
    }

    private void rej(String input) {
        assertFalse("We must reject \"" + input + '"', parse(input));
    }

    private static final ExceptionErrorListener listener = new ExceptionErrorListener();

    public static boolean parse(String input) {
        ExprLexer lexer = new ExprLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(listener);
        try {
            parser.init();
            return tokens.LA(1) == -1;
        } catch (Exception e) {
            return false;
        }
    }
}

