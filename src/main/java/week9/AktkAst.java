package week9;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week8.AktkLexer;
import week8.AktkParser;
import week9.ast.AstNode;

import java.io.IOException;


public class AktkAst {

    // Ise testimiseks soovitame kasutada selline meetod: sample.aktk failis sisend muuta.
    // Kui testid sinna kopeerida, siis äkki võtab IDE escape sümbolid ära ja on selgem,
    // milline see kood tegelikult välja näeb.
    public static void main(String[] args) throws IOException {
        AktkLexer lexer = new AktkLexer(CharStreams.fromFileName("sample.aktk"));
        AktkParser parser = new AktkParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.programm();
        System.out.println(tree.toStringTree(parser));
        AstNode ast = parseTreeToAst(tree);
        System.out.println(ast);
    }

    // Automaattestide jaoks vajalik meetod.
    public static AstNode createAst(String program) {
        AktkLexer lexer = new AktkLexer(CharStreams.fromString(program));
        AktkParser parser = new AktkParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.programm();
        return parseTreeToAst(tree);
    }

    // Põhimeetod, mida tuleks implementeerida:
	private static AstNode parseTreeToAst(ParseTree tree) {
		throw new UnsupportedOperationException();
	}
}
