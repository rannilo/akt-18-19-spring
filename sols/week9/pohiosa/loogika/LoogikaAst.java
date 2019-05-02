package week9.pohiosa.loogika;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week9.pohiosa.LoogikaBaseVisitor;
import week9.pohiosa.LoogikaLexer;
import week9.pohiosa.LoogikaParser;
import week9.pohiosa.loogika.loogikaAst.LoogikaNode;

import static week9.pohiosa.LoogikaParser.*;
import static week9.pohiosa.loogika.loogikaAst.LoogikaNode.*;

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
        LoogikaBaseVisitor<LoogikaNode> visitor = new LoogikaBaseVisitor<LoogikaNode>() {
            @Override
            public LoogikaNode visitInit(InitContext ctx) {
                return visit(ctx.avaldis());
            }

            @Override
            public LoogikaNode visitLiteraal(LiteraalContext ctx) {
                boolean val = ctx.getText().equals("1");
                return lit(val);
            }

            @Override
            public LoogikaNode visitMuutuja(MuutujaContext ctx) {
                return var(ctx.getText());
            }

            @Override
            public LoogikaNode visitSulud(SuludContext ctx) {
                return visit(ctx.avaldis());
            }

            @Override
            public LoogikaNode visitOp(OpContext ctx) {
                LoogikaNode vasak = visit(ctx.getChild(0));
                LoogikaNode parem = visit(ctx.getChild(2));
                switch (ctx.getChild(1).getText()) {
                    case "=":
                        return vordus(vasak, parem);
                    case "VOI":
                        return voi(vasak, parem);
                    default:
                        return ja(vasak, parem);
                }
            }

            @Override
            public LoogikaNode visitKuiSiis(KuiSiisContext ctx) {
                LoogikaNode kui = visit(ctx.avaldis(0));
                LoogikaNode siis = visit(ctx.avaldis(1));
                LoogikaNode muidu = null;
                if (ctx.avaldis(2) != null) muidu = visit(ctx.avaldis(2));
                return kuiSiis(kui, siis, muidu);
            }

            // Triviaalne käsitletakse BaseVisitor-is vaikimisi
        };

        return visitor.visit(tree);
    }
}
