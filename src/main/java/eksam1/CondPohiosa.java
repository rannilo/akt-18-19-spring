package eksam1;

import eksam1.ast.CondNode;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import static eksam1.ast.CondNode.*;

public class CondPohiosa {

    public static CondNode makeCondAst(String sisend) {
        CondLexer lexer = new CondLexer(CharStreams.fromString(sisend));
        CondParser parser = new CondParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy()); // vigase sisendi puhul viskab kohe erind.
        ParseTree defs = parser.init();
        //System.out.println(defs.toStringTree(parser));
        return parseTreetoAst(defs);
    }

    // Implementeerida parsepuu -> AST teisendus!
    private static CondNode parseTreetoAst(ParseTree tree) {
        return null;
    }

    public static void main(String[] args) {
        String sisend =
                "x on bool! y on int!\n" +
                "Arvuta: \n" +
                "  2 * \n" +
                "  Kas 5 + 5 on 10? \n" +
                "    Jah: Kas 5 + 5 on 10? \n" +
                "           Jah: 35 * Oota Oota 2 + 2 Valmis - 3 Valmis\n" +
                "           Ei: 100 \n" +
                "         Selge \n" +
                "    Ei: 100 \n" +
                "  Selge \n" +
                "+ 30 + Kas jah?\n" +
                "            Jah: 10\n" +
                "            Ei: 300\n" +
                "       Selge";

        System.out.println(makeCondAst(sisend));
    }

}


