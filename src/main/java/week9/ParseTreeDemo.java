package week9;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week8.AktkLexer;
import week8.AktkParser;

public class ParseTreeDemo {

    private static ParseTree createParseTree(String program) {
        AktkLexer lexer = new AktkLexer(CharStreams.fromString(program));
        AktkParser parser = new AktkParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.programm();
        System.out.println(tree.toStringTree(parser));
        return tree;
    }

    private static int evaluate(ParseTree tree) {
        throw new UnsupportedOperationException();
    }

    // testide jaoks...
    public static int evaluate(String expr) {
        AktkLexer lexer = new AktkLexer(CharStreams.fromString(expr));
        AktkParser parser = new AktkParser(new CommonTokenStream(lexer));
        return evaluate(parser.programm());
    }

    // Ise katsetamiseks:
    public static void main(String[] args) {
        ParseTree parseTree = createParseTree("5");
        // Enne järgmist võib ka breakpoint panna ja parsepuu debuugeris vaadata.
        // Seal on palju muud infot ka, aga children võib muudkui lahti klõpsutada!
        System.out.println(evaluate(parseTree));
    }

}
