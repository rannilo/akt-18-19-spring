package week9.pohiosa.letex;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week9.pohiosa.LetexBaseVisitor;
import week9.pohiosa.LetexLexer;
import week9.pohiosa.LetexParser;
import week9.pohiosa.letex.letAst.*;

import static week9.pohiosa.LetexParser.*;
import static week9.pohiosa.letex.letAst.LetAvaldis.*;

public class LetAst {
    public static void main(String[] args) {
        LetAvaldis avaldis = fabritseeriAvaldisAst("let x = 666 in (sum i = 0 to 3; j = 0 to i in i-j) - 1");
        System.out.println(avaldis);  // let("x", num(666), vahe(sum("i", num(0), num(3), sum("j", num(0), var("i"), vahe(var("i"), var("j")))), num(1)));

        // avaldise väljatrükki saad kasutada uue avaldiseASTi loomiseks (LetAvaldis staatilised meetodid)
        LetAvaldis avaldis1 = let("x", num(666), vahe(sum("i", num(0), num(3), sum("j", num(0), var("i"), vahe(var("i"), var("j")))), num(1)));
        System.out.println(avaldis.equals(avaldis1));  // true
    }

    /**
     * Vabrikumeetod avaldise abstraktse süntaksipuu loomiseks sõnest.
     * Jooksutab esmalt ANTLR-i ja kutsub allolev abimeetod, mida tuleks implementeerida.
     * NB! Ära unusta ANTLR-i koodi uuesti genereerida peale grammatika muutmist.
     */
    public static LetAvaldis fabritseeriAvaldisAst(String sisend) {
        LetexLexer lekser = new LetexLexer(CharStreams.fromString(sisend));
        LetexParser parser = new LetexParser(new CommonTokenStream(lekser));
        ParseTree konkreetnePuu = parser.init();
//        System.out.println(konkreetnePuu.toStringTree(parser));
        return fabritseeriAvaldisAst(konkreetnePuu);
    }

    /**
     * Abistav vabrikumeetod, mis teisendab avaldise parsepuu abstrakseks süntaksipuuks.
     *
     * @param puu ParseTree, mille tekitab teie loodud ANTLR grammatika
     * @return LetAvaldis - abstraktne süntakspuu
     */
    private static LetAvaldis fabritseeriAvaldisAst(ParseTree puu) {
        LetexBaseVisitor<LetAvaldis> visitor = new LetexBaseVisitor<LetAvaldis>() {
            @Override
            public LetAvaldis visitInit(InitContext ctx) {
                return visit(ctx.avaldis());
            }

            @Override
            public LetAvaldis visitArvuLiteraal(ArvuLiteraalContext ctx) {
                return new LetArv(Integer.parseInt(ctx.getText()));
            }

            @Override
            public LetAvaldis visitMuutujaNimi(MuutujaNimiContext ctx) {
                return new LetMuutuja(ctx.getText());
            }

            @Override
            public LetAvaldis visitSuluAvaldis(SuluAvaldisContext ctx) {
                return visit(ctx.avaldis());
            }

            @Override
            public LetAvaldis visitLahutamine(LahutamineContext ctx) {
                return new LetVahe(
                        visit(ctx.avaldis(0)),
                        visit(ctx.avaldis(1)));
            }

            @Override
            public LetAvaldis visitMuutujaSidumine(MuutujaSidumineContext ctx) {
                LetAvaldis avaldis = visit(ctx.body);
                for (int i = ctx.Muutuja().size()-1; i >= 0; i--) {
                    avaldis = new LetSidumine(
                            ctx.Muutuja(i).getText(),
                            visit(ctx.avaldis(i)),
                            avaldis);
                }
                return avaldis;
            }

            @Override
            public LetAvaldis visitSummeerimine(SummeerimineContext ctx) {
                LetAvaldis avaldis = visit(ctx.body);
                for (int i = ctx.Muutuja().size() - 1; i >= 0; i--) {
                    avaldis = new LetSumma(
                            ctx.Muutuja(i).getText(),
                            visit(ctx.lo.get(i)),
                            visit(ctx.hi.get(i)),
                            avaldis);
                }
                return avaldis;
            }
        };

        return visitor.visit(puu);
    }

}
