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
        UjukomaBaseVisitor<UjukomaNode> visitor = new UjukomaBaseVisitor<UjukomaNode>() {

            @Override
            public UjukomaNode visitInit(UjukomaParser.InitContext ctx) {
                return visit(ctx.avaldis());
            }

            @Override
            public UjukomaNode visitIntLit(UjukomaParser.IntLitContext ctx) {
                return lit(Integer.valueOf(ctx.getText()));
            }

            @Override
            public UjukomaNode visitIntVar(UjukomaParser.IntVarContext ctx) {
                return UjukomaNode.var(ctx.getText().charAt(0));
            }

            @Override
            public UjukomaNode visitArithI(UjukomaParser.ArithIContext ctx) {
                return op(ctx.op.getText().charAt(0),
                        visit(ctx.intavaldis(0)),
                        visit(ctx.intavaldis(1)));
            }

            @Override
            public UjukomaNode visitDblLit(UjukomaParser.DblLitContext ctx) {
                return lit(Double.valueOf(ctx.getText()));
            }

            @Override
            public UjukomaNode visitDblVar(UjukomaParser.DblVarContext ctx) {
                return UjukomaNode.var(ctx.getText().charAt(0));
            }

            @Override
            public UjukomaNode visitArithD(UjukomaParser.ArithDContext ctx) {
                return op(ctx.op.getText().charAt(0),
                        visit(ctx.dblavaldis(0)),
                        visit(ctx.dblavaldis(1)));
            }

            @Override
            public UjukomaNode visitParenI(UjukomaParser.ParenIContext ctx) {
                return visit(ctx.intavaldis());
            }

            @Override
            public UjukomaNode visitParenD(UjukomaParser.ParenDContext ctx) {
                return visit(ctx.dblavaldis());
            }
        };

        return visitor.visit(tree);
    }
}
