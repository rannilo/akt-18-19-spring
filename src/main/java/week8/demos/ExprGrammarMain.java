package week8.demos;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class ExprGrammarMain {

    public static String parse(String input) {
        ExprLexer lexer = new ExprLexer(CharStreams.fromString(input));
        ExprParser parser = new ExprParser(new CommonTokenStream(lexer));
        return parser.expr().toStringTree(parser);
    }

    public static void main(String[] args) {
        System.out.println(parse("x+x*x"));
    }
}
