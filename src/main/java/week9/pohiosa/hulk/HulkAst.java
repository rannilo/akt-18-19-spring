package week9.pohiosa.hulk;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week9.pohiosa.HulkLexer;
import week9.pohiosa.HulkParser;
import week9.pohiosa.hulk.hulkAst.HulkNode;

public class HulkAst {

    public static void main(String[] args) {
        HulkNode ln = makeHulkAst("A := B");
        System.out.println(ln);  // A := B
    }

    public static HulkNode makeHulkAst(String input) {
        HulkLexer lexer = new HulkLexer(CharStreams.fromString(input));
        HulkParser parser = new HulkParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.init();
//        System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    // Implementeeri see meetod.
    private static HulkNode parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }
}
