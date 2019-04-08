package week7.kalaparser;

import week6.kalalexer.Lexer;
import week6.kalalexer.Token;
import week6.kalalexer.TokenType;

import java.util.List;

import static week6.kalalexer.TokenType.EOF;

public class KalaParser {

    private final List<Token> tokens;
    private int pos;

    private KalaParser(Lexer lexer) {
        this.tokens = lexer.readAllTokens();
        this.pos = 0;
    }

    public static KalaNode parse(String input) {
        Lexer lexer = new Lexer(input);
        // Ja kuidagi võiks nüüd seda lekserit kasutada oma parseris...
        KalaParser parser = new KalaParser(lexer);
        return parser.parse();
    }

    private KalaNode parse() {
        KalaNode node = s();
        done();
        return node;
    }

    private void done() {
        if (peek().getType() != EOF) throw new KalaParseException(peek(), EOF);
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private void match(TokenType tokenType) {
        if (peek().getType() != tokenType) {
            throw new KalaParseException(peek(), tokenType);
        }
        pos++;
    }


    // S → (L) | ()
    // L → A,L | A
    // A → w | 0 | S
    private KalaNode s() {
        return null;
    }

    public static void main(String[] args) {
        KalaNode ast = parse("(kala, (x,y  , null, (), (kala,()) ))");
        System.out.println(ast);
    }
}
