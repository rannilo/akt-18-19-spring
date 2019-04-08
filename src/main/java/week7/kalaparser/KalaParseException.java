package week7.kalaparser;

import week6.kalalexer.Token;
import week6.kalalexer.TokenType;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class KalaParseException extends RuntimeException {
	private final Token token;
    private final Set<TokenType> expected;

    public Token getToken() {
        return token;
    }

    public Set<TokenType> getExpected() {
        return expected;
    }

    public KalaParseException(Token tok, TokenType... expected) {
        super("Unexpected token \'" + tok + ", but expected: " + Arrays.toString(expected));
        this.token = tok;
        this.expected = new LinkedHashSet<>(Arrays.asList(expected));
    }

}
