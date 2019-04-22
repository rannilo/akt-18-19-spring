package week8;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;


public class AktkGrammarMain {

    public static String parse(String input) {
        AktkLexer lexer = new AktkLexer(CharStreams.fromString(input));
        AktkParser parser = new AktkParser(new CommonTokenStream(lexer));
        return parser.programm().toStringTree(parser);
    }

    public static void main(String[] args) {
        System.out.println(parse("print(\"Hello, world!\")"));
    }
}
