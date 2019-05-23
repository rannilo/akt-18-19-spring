package week11.eksamdemo.choice;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import week11.eksamdemo.choice.ChoiceParser.AddContext;
import week11.eksamdemo.choice.ChoiceParser.ChoiceContext;
import week11.eksamdemo.choice.ChoiceParser.IntContext;
import week11.eksamdemo.choice.ChoiceParser.ParensContext;
import week11.eksamdemo.choice.choiceAst.ChoiceNode;

public class ChoicePohiosa {

    public static ChoiceNode makeChoiceAst(String sisend) {
        ChoiceLexer lexer = new ChoiceLexer(CharStreams.fromString(sisend));
        ChoiceParser parser = new ChoiceParser(new CommonTokenStream(lexer));
        ParseTree parseTree = parser.expr();
        return parseTreeToAst(parseTree);
    }

    private static ChoiceNode parseTreeToAst(ParseTree parseTree) {
        return new ChoiceBaseVisitor<ChoiceNode>() {
            @Override
            public ChoiceNode visitAdd(AddContext ctx) {
                return ChoiceNode.add(visit(ctx.expr(0)), visit(ctx.expr(1)));
            }

            @Override
            public ChoiceNode visitParens(ParensContext ctx) {
                return visit(ctx.expr());
            }

            @Override
            public ChoiceNode visitChoice(ChoiceContext ctx) {
                return ChoiceNode.choice(visit(ctx.expr(0)), visit(ctx.expr(1)));
            }

            @Override
            public ChoiceNode visitInt(IntContext ctx) {
                return ChoiceNode.val(Integer.valueOf(ctx.getText()));
            }
        }.visit(parseTree);
    }

    public static void main(String[] args) {
        String sisend = "(1|2) + 3 | 5";
        System.out.println(makeChoiceAst(sisend)); // choice(add(choice(1,2),3),5)
    }
}
