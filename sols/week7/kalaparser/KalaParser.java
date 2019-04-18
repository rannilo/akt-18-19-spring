package week7.kalaparser;

import week6.kalalexer.Lexer;
import week6.kalalexer.Token;
import week6.kalalexer.TokenType;

import java.util.ArrayList;
import java.util.List;

import static week6.kalalexer.TokenType.*;

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
        match(LPAREN);
        if (peek().getType() == RPAREN) {
            match(RPAREN);
            return KalaNode.mkList();
        } else {
            List<KalaNode> list = l();
            match(RPAREN);
            return KalaNode.mkList(list);
        }
    }

    private List<KalaNode> l() {
        List<KalaNode> list = new ArrayList<>();
        KalaNode left = a();
        list.add(left);
        if (peek().getType() == COMMA) {
            match(COMMA);
            list.addAll(l());
            return list;
        }
        return list;
    }

    // Variantina võib seda iteratiivselt teha.
    // L → A,L | A esitame EBNF kujul L → A(,A)*
    private List<KalaNode> l_ebnf() {
        List<KalaNode> list = new ArrayList<>();
        KalaNode left = a();
        list.add(left);
        while (peek().getType() == COMMA) {
            match(COMMA);
            list.add(a());
        }
        return list;
    }

    private KalaNode a() {
        Token node = peek();
        switch (node.getType()) {
            case IDENT:
                match(IDENT);
                return KalaNode.mkIdent((String) node.getData());
            case NULL:
                match(NULL);
                return KalaNode.mkNull();
            case LPAREN:
                return s();
            default:
                throw new KalaParseException(peek(), IDENT, NULL, LPAREN);
        }
    }

    public static void main(String[] args) {
        KalaNode ast = parse("(kala, (x,y  , null, (), (kala,()) ))");
        System.out.println(ast);
    }
}
