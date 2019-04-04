package week6.kalalexer;

import java.util.ArrayList;
import java.util.List;

import static week6.kalalexer.TokenType.*;

public class Lexer {
    private static final char TERMINATOR = '\0';
    private final String input;
    private int pos;

    public Lexer(String input) {
        this.input = input + TERMINATOR;
        pos = 0;
    }

    public List<Token> readAllTokens() {
        List<Token> tokens = new ArrayList<>();
        while (peek() != TERMINATOR) {
            switch (peek()) {
                case '(':
                    tokens.add(new Token(LPAREN));
                    pos++;
                    break;
                case ')':
                    tokens.add(new Token(RPAREN));
                    pos++;
                    break;
                case ',':
                    tokens.add(new Token(COMMA));
                    pos++;
                    break;
                default:
                    if (Character.isLetter(peek())) {
                        Token tok = readIdentOrNull();
                        tokens.add(tok);
                    } else pos++;
            }
        }
        tokens.add(new Token(EOF));
        return tokens;
    }

    private Token readIdentOrNull() {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(peek())) {
            sb.append(peek());
            pos++;
        }
        if (sb.toString().equals("null")) {
            return new Token(NULL);
        } else
            return new Token(IDENT, sb.toString());
    }

    private char peek() {
        return input.charAt(pos);
    }
}