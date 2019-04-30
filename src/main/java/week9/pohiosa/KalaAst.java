package week9.pohiosa;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week7.kalaparser.KalaNode;

import java.util.HashMap;
import java.util.Map;

public class KalaAst {

    public static KalaNode makeKalaAst(String sisend) {
        KalaLexer lexer = new KalaLexer(CharStreams.fromString(sisend));
        KalaParser parser = new KalaParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.init();
        return parseTreeToAst(tree);
    }

    // Põhimeetod, mida tuleks implementeerida
    private static KalaNode parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        KalaNode kalaAst = makeKalaAst("(kala, (x,y , null, (), (kala,()) ))");
        System.out.println(kalaAst);  // (kala, (x, y, NULL, (), (kala, ())))

        Map<String, Integer> env = new HashMap<>();
        env.put("kala", 1);
        env.put("x", 2);
        env.put("y", 3);

        System.out.println(kalaAst.sum(env));  // 7
    }
}
