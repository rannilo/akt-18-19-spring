package week11.eksamdemo.choice;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week11.eksamdemo.choice.choiceAst.ChoiceNode;

public class ChoicePohiosa {

    public static ChoiceNode makeChoiceAst(String sisend) {
        ChoiceLexer lexer = new ChoiceLexer(CharStreams.fromString(sisend));
        ChoiceParser parser = new ChoiceParser(new CommonTokenStream(lexer));
        ParseTree parseTree = parser.expr();
        return parseTreeToAst(parseTree);
    }

    private static ChoiceNode parseTreeToAst(ParseTree parseTree) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        String sisend = "(1|2) + 3 | 5";
        System.out.println(makeChoiceAst(sisend)); // choice(add(choice(1,2),3),5)
    }
}
