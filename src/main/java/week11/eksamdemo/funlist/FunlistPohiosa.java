package week11.eksamdemo.funlist;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week11.eksamdemo.funlist.ast.FunlistProg;

public class FunlistPohiosa {

    public static FunlistProg makeFlistAst(String sisend) {
        FunlistLexer lexer = new FunlistLexer(CharStreams.fromString(sisend));
        FunlistParser parser = new FunlistParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy()); // vigase sisendi puhul viskab kohe erind.
        ParseTree defs = parser.defs();
        //System.out.println(defs.toStringTree(parser));
        return parseTreetoFlistAst(defs);
    }

    // Implementeerida parsepuu -> AST teisendus!
    private static FunlistProg parseTreetoFlistAst(ParseTree tree) {
        return null;
    }

    public static void main(String[] args) {
        String sisend = "fun(x) = 10\n" + "id(y) = y";
        System.out.println(makeFlistAst(sisend));
    }

}


