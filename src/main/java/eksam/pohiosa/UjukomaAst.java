package eksam.pohiosa;

import eksam.pohiosa.ujukomaAst.UjukomaNode;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import static eksam.pohiosa.ujukomaAst.UjukomaNode.*;


public class UjukomaAst {

    public static void main(String[] args) {
        UjukomaNode ln = MakeUjukomaAst("2*3+4*i");
        UjukomaNode ex = op('+',
                op('*', lit(2), lit(3)),
                op('*', lit(4), var('i')));
        System.out.println(ln.equals(ex));
    }

    public static UjukomaNode MakeUjukomaAst(String input) {
        UjukomaLexer lexer = new UjukomaLexer(CharStreams.fromString(input));
        UjukomaParser parser = new UjukomaParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    // Implementeeri see meetod.
    private static UjukomaNode parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }
}
