package week7;

import week6.Token;
import week6.TokenType;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 * Seda klassi ei ole vaja muuta ega esitada
 */
public class AKTKParseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final Token token;
    private final Set<TokenType> expected;

    public Token getToken() {
        return token;
    }

    public Set<TokenType> getExpected() {
        return expected;
    }

    public AKTKParseException(Token tok, TokenType... expected) {
        super("Unexpected token \'" + tok + "\' but expected: " + Arrays.toString(expected));
        this.token = tok;
        this.expected = new LinkedHashSet<>(Arrays.asList(expected));
    }

    // Kui ei soovi täpsema veatöötlusega tegeleda.
    public AKTKParseException() {
        token = null;
        expected = null;
    }

}
