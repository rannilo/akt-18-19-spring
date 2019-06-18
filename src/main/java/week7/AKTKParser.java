package week7;

import week6.Token;
import week6.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static week6.TokenType.*;

public class AKTKParser {

    private final List<Token> tokens;
    private int pos;
    private int rparenNeeded = 0;

    private AKTKParser(Lexer lexer) {
        this.tokens = lexer.readAllTokens();
        this.pos = 0;
    }

    public static ExprNode parse(String input) {
        Lexer lexer = new Lexer(input);
        AKTKParser parser = new AKTKParser(lexer);
        return parser.parse();
    }

    private ExprNode parse() {
        ExprNode node = avaldis();
        done();
        return node;
    }

    private ExprNode avaldis() {
        ExprNode node;
        ExprNode l = term();
        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<ExprNode> exprNodes = new ArrayList<>();
        exprNodes.add(l);
        ArrayList<TokenType> unacceptedTokens = new ArrayList(Arrays.asList(INTEGER, VARIABLE, LPAREN));
        if(unacceptedTokens.contains(peek().getType()) || (peek().getType()==RPAREN && rparenNeeded == 0)){
            if(rparenNeeded > 0) throw new AKTKParseException(peek(), PLUS, MINUS, TIMES, DIV, RPAREN);
            else throw new AKTKParseException(peek(), PLUS, MINUS, TIMES, DIV, EOF);
        }
        while(peek().getType() == PLUS || peek().getType() == MINUS){
            tokens.add(peek());
            match(peek().getType());
            exprNodes.add(term());
        }
        node = bottomUpListToNode(tokens, exprNodes);
        return node;
    }

    private ExprNode term() {
        ExprNode node;
        ExprNode l = faktor();
        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<ExprNode> exprNodes = new ArrayList<>();
        exprNodes.add(l);
        while(peek().getType() == TIMES || peek().getType() == DIV){
            tokens.add(peek());
            match(peek().getType());
            exprNodes.add(faktor());
        }
        node = bottomUpListToNode(tokens, exprNodes);
        return node;
    }

    private ExprNode faktor() {
        ExprNode node;
        switch (peek().getType()){
            case LPAREN:
                match(LPAREN);
                rparenNeeded++;
                node = avaldis();
                match(RPAREN);
                rparenNeeded--;
                break;
            case VARIABLE:
                node = ExprNode.var((String)peek().getData());
                match(VARIABLE);
                break;
            case INTEGER:
                node = ExprNode.num((int)peek().getData());
                match(INTEGER);
                break;
            default:
                throw new AKTKParseException(peek(), LPAREN, VARIABLE, INTEGER);
        }
        return node;
    }

    private ExprNode bottomUpListToNode(ArrayList<Token> tokens, ArrayList<ExprNode> exprNodes) {
        if(exprNodes.size() == 0){
            return null;
        }
        else if(exprNodes.size() == 1){
            return exprNodes.get(0);
        }
        else if(exprNodes.size() == 2){
            return mkNode(tokens.get(0), exprNodes.get(0), exprNodes.get(1));
        }
        else{
            int size = exprNodes.size();
            ExprNode left = exprNodes.get(0);
            ExprNode right = exprNodes.get(1);
            for(int i = 2; i<size; i++){
                left = mkNode(tokens.get(i-2), left, right);
                right = exprNodes.get(i);
            }
            return mkNode(tokens.get(tokens.size()-1), left, right);
        }
    }

    private ExprNode mkNode(Token token, ExprNode left, ExprNode right) {
        switch (token.getType()){
            case PLUS:
                return ExprNode.plus(left, right);
            case MINUS:
                return ExprNode.minus(left, right);
            case TIMES:
                return ExprNode.mul(left, right);
            case DIV:
                return ExprNode.div(left, right);
            default:
                throw new AKTKParseException(token, PLUS, MINUS, TIMES, DIV);
        }
    }

    private void match(TokenType type) {
        if(type != peek().getType()){
            throw new AKTKParseException(peek(), type);
        }
        pos++;
    }


    private void done() {
        if(peek().getType() != EOF) throw new AKTKParseException(peek(), EOF);
    }

    private Token peek(){
        return tokens.get(pos);
    }

}