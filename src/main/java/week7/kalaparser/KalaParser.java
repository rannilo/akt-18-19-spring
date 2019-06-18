package week7.kalaparser;

import org.eclipse.jdt.internal.core.util.RuntimeInvisibleTypeAnnotationsAttribute;
import week6.kalalexer.Lexer;
import week6.kalalexer.Token;
import week6.kalalexer.TokenType;

import java.util.ArrayList;
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
    // L → A(,A)* EBNF notatsioon
    // A → w | 0 | S
    private KalaNode s() {
        KalaNode node = KalaNode.mkList();
        match(TokenType.LPAREN);
        if(peek().getType() != TokenType.RPAREN){
            node = l();
        }
        match(TokenType.RPAREN);
        return node;
    }

    private KalaNode l(){
        KalaNode node = a();
        List<KalaNode> elemendid = new ArrayList<>();
        elemendid.add(a());
        while(peek().getType() == TokenType.COMMA){
            match(TokenType.COMMA);
            elemendid.add(a());
        }
        if(peek().getType() == TokenType.COMMA){
            match(TokenType.COMMA);
            l();
        }
        return node;
    }

    private KalaNode a(){
        KalaNode node = null;
        switch (peek().getType()){
            case IDENT:
                Token current = peek();
                match(TokenType.IDENT);
                node = KalaNode.mkIdent((String)current.getData());
                break;
            case NULL:
                match(TokenType.NULL);
                node = KalaNode.mkNull();
                break;
            case LPAREN:
                node = s();
                break;
            default:
                throw new RuntimeException("Väär");
        }
        return node;
    }

    public static void main(String[] args) {
        KalaNode ast = parse("(kala, (x,y  , null, (), (kala,()) ))");
        System.out.println(ast);
    }
}
