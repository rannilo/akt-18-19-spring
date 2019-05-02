package week9.pohiosa.hulk;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week9.pohiosa.HulkBaseVisitor;
import week9.pohiosa.HulkLexer;
import week9.pohiosa.HulkParser;
import week9.pohiosa.hulk.hulkAst.HulkLause;
import week9.pohiosa.hulk.hulkAst.HulkNode;
import week9.pohiosa.hulk.hulkAst.HulkTingimus;
import week9.pohiosa.hulk.hulkAst.avaldis.HulkAvaldis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static week9.pohiosa.HulkParser.*;
import static week9.pohiosa.hulk.hulkAst.HulkNode.*;

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
        HulkBaseVisitor<HulkNode> visitor = new HulkBaseVisitor<HulkNode>() {
            @Override
            public HulkNode visitInit(InitContext ctx) {
                return visit(ctx.programm());
            }

            @Override
            public HulkNode visitProgramm(ProgrammContext ctx) {
                List<HulkLause> laused = new ArrayList<>();
                ctx.lause().forEach(x -> laused.add((HulkLause) visit(x)));
                return prog(laused);
            }

            @Override
            public HulkNode visitLause(LauseContext ctx) {
                Character nimi = ctx.Hulk().getText().charAt(0);
                HulkAvaldis avaldis = (HulkAvaldis) visit(ctx.getChild(2));
                HulkTingimus tingimus = null;
                if (ctx.tingimus() != null) tingimus = (HulkTingimus) visit(ctx.tingimus());

                if (ctx.op.getText().equals("<-")) avaldis = tehe(var(nimi), avaldis, '+');
                else if (ctx.op.getText().equals("->")) avaldis = tehe(var(nimi), avaldis, '-');

                return lause(nimi, avaldis, tingimus);
            }

            @Override
            public HulkNode visitSubset(SubsetContext ctx) {
                HulkAvaldis alamAvaldis = (HulkAvaldis) visit(ctx.avaldis(0));
                HulkAvaldis ylemAvaldis = (HulkAvaldis) visit(ctx.avaldis(1));
                return ting(alamAvaldis, ylemAvaldis);
            }

            @Override
            public HulkNode visitIncl(InclContext ctx) {
                HulkAvaldis alamAvaldis = lit(ctx.Element().getText().charAt(0));  // moodustan yhe-el hulga
                HulkAvaldis ylemAvaldis = (HulkAvaldis) visit(ctx.avaldis());
                return ting(alamAvaldis, ylemAvaldis);
            }

            @Override
            public HulkNode visitElementideList(ElementideListContext ctx) {
                Set<Character> elemendid = new HashSet<>();
                ctx.Element().forEach(x -> elemendid.add(x.getText().charAt(0)));
                return lit(elemendid);
            }

            @Override
            public HulkNode visitMuutuja(MuutujaContext ctx) {
                return var(ctx.getText().charAt(0));
            }

            @Override
            public HulkNode visitLiteraal(LiteraalContext ctx) {
                if (ctx.elementideList() != null) return visit(ctx.elementideList());
                else return lit();
            }

            @Override
            public HulkNode visitSulud(SuludContext ctx) {
                return visit(ctx.avaldis());
            }

            @Override
            public HulkNode visitTehe(TeheContext ctx) {
                HulkAvaldis vasak = (HulkAvaldis) visit(ctx.avaldis(0));
                HulkAvaldis parem = (HulkAvaldis) visit(ctx.avaldis(1));
                Character op = ctx.getChild(1).getText().charAt(0);
                return tehe(vasak, parem, op);
            }
        };

        return visitor.visit(tree);
    }
}
