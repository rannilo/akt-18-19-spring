package week7;

import week6.Lexer;
import week6.Token;
import week6.TokenType;

import java.io.StringReader;

import static week6.TokenType.*;

public class AKTKParser {

    private Lexer lex;
    private Token current;

    public AKTKParser(Lexer lex) {
        this.lex = lex;
    }

    private void consume() {
        current = lex.readNextToken();
    }

    private TokenType peek() {
        return current.getType();
    }

    private void match(TokenType t) {
        if (peek() != t) throw new AKTKParseException(current, t);
        consume();
    }

    private ExprNode avaldis() {
        ExprNode left = term();
        TokenType next = peek();
        while (next == PLUS || next == MINUS) {
            consume();
            left = new ExprNode.BinOp(next, left, term());
            next = peek();
        }
        if (next == EOF || next == RPAREN) {
            return left;
        }
        throw new AKTKParseException(current, PLUS, MINUS, TIMES, DIV, EOF, RPAREN);
    }

    private ExprNode term() {
        ExprNode left = faktor();
        TokenType next = peek();
        while (next == TIMES || next == DIV) {
            consume();
            left = new ExprNode.BinOp(next, left, faktor());
            next = peek();
        }
        return left;
    }

    private ExprNode faktor() {
        TokenType t = peek();
        ExprNode result;
        switch (t) {
            case VARIABLE:
                result = new ExprNode.Variable((String) current.getData());
                consume();
                break;
            case INTEGER:
                result = new ExprNode.IntLiteral((int) current.getData());
                consume();
                break;
            case LPAREN:
                match(LPAREN);
                result = avaldis();
                match(RPAREN);
                break;
            default:
                throw new AKTKParseException(current, VARIABLE, INTEGER, LPAREN);
        }
        return result;
    }

    public ExprNode parse() {
        consume();
        ExprNode result = avaldis();
        if (peek() != EOF) {
            throw new AKTKParseException(current, EOF);
        }
        return result;
    }

    public static ExprNode parse(String input) {
        Lexer lex = new Lexer(new StringReader(input));
        AKTKParser parser = new AKTKParser(lex);
        return parser.parse();
    }

}
