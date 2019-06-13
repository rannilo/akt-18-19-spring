package eksam2;

import eksam2.ast.BologNode;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashSet;
import java.util.Set;

public class BologPohiosa {


    public static Set<BologNode> makeBologAst(String sisend) {
        BologLexer lexer = new BologLexer(CharStreams.fromString(sisend));
        BologParser parser = new BologParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy()); // vigase sisendi puhul viskab kohe erind.
        ParseTree defs = parser.init();
        //System.out.println(defs.toStringTree(parser));
        return parseTreetoAst(defs);
    }

    // Implementeerida parsepuu -> AST teisendus!
    private static Set<BologNode> parseTreetoAst(ParseTree tree) {
        Set<BologNode> nodes = new HashSet<>();
        return nodes;
    }


    public static void main(String[] args) {
        String input = "X kui P ja Q\n" +
                "X && P kui P, P||Q ja P kui Q ja R\n" +
                "!X && P kui R && JAH || EI\n";

        Set<BologNode> bologNodes = makeBologAst(input);
        for (BologNode bologNode : bologNodes) System.out.println(bologNode);
    }

}
