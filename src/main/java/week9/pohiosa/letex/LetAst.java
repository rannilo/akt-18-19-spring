package week9.pohiosa.letex;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week9.pohiosa.LetexLexer;
import week9.pohiosa.LetexParser;
import week9.pohiosa.letex.letAst.*;

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
        throw new UnsupportedOperationException();
    }

}
