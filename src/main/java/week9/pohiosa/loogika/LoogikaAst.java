package week9.pohiosa.loogika;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week9.pohiosa.LoogikaLexer;
import week9.pohiosa.LoogikaParser;
import week9.pohiosa.loogika.loogikaAst.LoogikaNode;

public class LoogikaAst {

    public static void main(String[] args) {
        LoogikaNode ln = makeLoogikaAst("1");
        System.out.println(ln);  // 1
        //LoogikaNode newNode = makeLoogikaAst(ln.toString());
        //System.out.println(ln.equals(newNode));  // true
    }

    public static LoogikaNode makeLoogikaAst(String input) {
        LoogikaLexer lexer = new LoogikaLexer(CharStreams.fromString(input));
        LoogikaParser parser = new LoogikaParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    // Implementeeri see meetod.
    private static LoogikaNode parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }
}
