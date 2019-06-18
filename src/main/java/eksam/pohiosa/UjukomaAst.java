package eksam.pohiosa;

import eksam.pohiosa.ujukomaAst.UjukomaNode;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import static eksam.pohiosa.ujukomaAst.UjukomaNode.*;
import static eksam.pohiosa.UjukomaParser.*;


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
        UjukomaBaseVisitor<UjukomaNode> visitor = new UjukomaBaseVisitor<>(){
            @Override
            public UjukomaNode visitInit(InitContext context){
                return visit(context.avaldis());
            }
            @Override
            public UjukomaNode visitTaisMuutuja(TaisMuutujaContext context){
                return visitMuutuja(context);
            }
            @Override
            public UjukomaNode visitUjuMuutuja(UjuMuutujaContext context){
                return visitMuutuja(context);
            }
            private UjukomaNode visitMuutuja(ParserRuleContext context){
                return UjukomaNode.var(context.getChild(0).getText().charAt(0));
            }
            @Override
            public UjukomaNode visitTaisLiteraal(TaisLiteraalContext context){
                return UjukomaNode.lit(Integer.parseInt(context.getText()));
            }
            @Override
            public UjukomaNode visitUjuLiteraal(UjuLiteraalContext context){
                return UjukomaNode.lit(Double.parseDouble(context.getText()));
            }
            @Override
            public UjukomaNode visitTaisBinOp(TaisBinOpContext context){
                return binaarneOperatsioon(context);
            }
            @Override
            public UjukomaNode visitUjuBinOp(UjuBinOpContext context){
                return binaarneOperatsioon(context);
            }
            @Override
            public UjukomaNode visitTaisSulud(TaisSuludContext context){
                return visitSulud(context);
            }
            @Override
            public UjukomaNode visitUjuSulud(UjuSuludContext context){
                return visitSulud(context);
            }
            private UjukomaNode visitSulud(ParserRuleContext context){
                return visit(context.getChild(1));
            }
            private UjukomaNode binaarneOperatsioon(ParserRuleContext ctx) {
                Character operaator = ctx.getChild(1).getText().charAt(0);
                UjukomaNode vasakArgument = visit(ctx.getChild(0));
                UjukomaNode paremArgument = visit(ctx.getChild(2));
                return UjukomaNode.op(operaator, vasakArgument, paremArgument);
            }
        };
        return visitor.visit(tree);
    }
}
