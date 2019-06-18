package week9.pohiosa.letex;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import week9.pohiosa.LetexBaseVisitor;
import week9.pohiosa.LetexLexer;
import week9.pohiosa.LetexParser;
import week9.pohiosa.letex.letAst.*;
import week9.pohiosa.LetexParser.*;

import java.util.List;

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
        LetexBaseVisitor<LetAvaldis> visitor = new LetexBaseVisitor<>(){
            @Override
            public LetAvaldis visitMuutuja(MuutujaContext context){
                return LetAvaldis.var(context.getText());
            }
            @Override
            public LetAvaldis visitTaisarv(TaisarvContext context){
                return LetAvaldis.num(Integer.parseInt(context.getText()));
            }
            @Override
            public LetAvaldis visitLahutamine(LahutamineContext context){
                return LetAvaldis.vahe(visit(context.avaldis(0)), visit(context.avaldis(1)));
            }
            @Override
            public LetAvaldis visitInit(InitContext context){
                return visit(context.getChild(0));
            }
            @Override
            public LetAvaldis visitSulud(SuludContext context){
                return visit(context.avaldis());
            }
            @Override
            public LetAvaldis visitSidumine(SidumineContext context){
                List<Token> muutujad = context.muutujad;
                AvaldisContext keha = context.keha;
                List<AvaldisContext> avaldised = context.avaldised;
                LetAvaldis avaldis = LetAvaldis.let(muutujad.get(muutujad.size()-1).getText(), visit(avaldised.get(avaldised.size()-1)), visit(keha));
                for(int i = avaldised.size()-2; i>=0; i--){
                    avaldis = LetAvaldis.let(muutujad.get(i).getText(), visit(avaldised.get(i)), avaldis);
                }
                return avaldis;
            }
            @Override
            public LetAvaldis visitSummeerimine(SummeerimineContext context){
                LetAvaldis avaldis = visit(context.keha);
                for(int i = context.MUUTUJA().size()-1; i>=0; i--){
                    avaldis = LetAvaldis.sum(context.MUUTUJA(i).getText(), visit(context.lo.get(i)), visit(context.hi.get(i)), avaldis);
                }
                return avaldis;
            }
        };
        return visitor.visit(puu);
    }

}
