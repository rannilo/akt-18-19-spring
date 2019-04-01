package week6;

import org.junit.Test;
import week6.kalalexer.Lexer;
import week6.kalalexer.Token;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class KalaLexerTest {

    @Test
    public void testReadAllTokens1() throws Exception {
        check("", "[<EOF>]");
        check("()", "[<LPAREN>, <RPAREN>, <EOF>]");
    }

    @Test
    public void testReadAllTokens2() throws Exception {
        check("(x, null)", "[<LPAREN>, <IDENT:x>, <COMMA>, <NULL>, <RPAREN>, <EOF>]");
    }

    @Test
    public void testReadAllTokens3() throws Exception {
        check("(kala, x, null)", "[<LPAREN>, <IDENT:kala>, <COMMA>, <IDENT:x>, <COMMA>, <NULL>, <RPAREN>, <EOF>]");
    }

    private void check(String input, String expected) {
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.readAllTokens();
        assertEquals(expected, tokens.toString());
    }
}