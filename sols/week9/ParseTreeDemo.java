package week9;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import week8.AktkBaseVisitor;
import week8.AktkLexer;
import week8.AktkParser;
import week8.AktkParser.ArvuliteraalContext;
import week8.AktkParser.KorrutamineJagamineContext;
import week8.AktkParser.LiitmineLahutamineContext;

public class ParseTreeDemo {

    private static ParseTree createParseTree(String program) {
        AktkLexer lexer = new AktkLexer(CharStreams.fromString(program));
        AktkParser parser = new AktkParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.programm();
        System.out.println(tree.toStringTree(parser));
        return tree;
    }

    private static int evaluate(ParseTree tree) {
        AktkBaseVisitor<Integer> visitor = new AktkBaseVisitor<Integer>() {
            // Tipp tüübiga ArvuliteraalContext vastab grammatikas
            // märgendile Arvuliteraal.
            // Siin tuleb lihtsalt küsida selle tipu tekst ja teisendada
            // see täisarvuks
            @Override
            public Integer visitArvuliteraal(ArvuliteraalContext ctx) {
                return Integer.parseInt(ctx.getText());
            }

            // Järgmise juhtumi mõistmiseks otsi grammatikast üles
            // sildid KorrutamineJagamine ja LiitmineLahutamine --
            // loodetavasti on siis arusaadav, miks siin just nii toimitakse.
            @Override
            public Integer visitKorrutamineJagamine(KorrutamineJagamineContext ctx) {
                return binaarneOperatsioon(ctx);
            }

            @Override
            public Integer visitLiitmineLahutamine(LiitmineLahutamineContext ctx) {
                return binaarneOperatsioon(ctx);
            }

            private Integer binaarneOperatsioon(ParserRuleContext ctx) {
                // küsin tipu alluvad
                ParseTree leftChild  = ctx.getChild(0);
                String operator      = ctx.getChild(1).getText();
                ParseTree rightChild = ctx.getChild(2);

                // väärtustan rekursiivselt
                int leftValue  = visit(leftChild);
                int rightValue = visit(rightChild);

                // väärtustan terve operatsiooni
                switch (operator) {
                    case "+": return leftValue + rightValue;
                    case "-": return leftValue - rightValue;
                    case "*": return leftValue * rightValue;
                    case "/": return leftValue / rightValue;
                    default : throw new RuntimeException("Tundmatu operaator");
                }
            }

            // Ülejäänud juhtumid käsitleb AktkBaseVisitor automaatselt
            // visit-ides kõiki alamtippe, sh neid mis antud ülesande
            // puhul tähtsat rolli ei mängi. Vaata jälle näidisavaldise
            // parse-puu graafilist esitust -- kui me alustame juurtipust,
            // siis me peame nende vahetippude (lause, avaldis, avaldis5, jne)
            // kaudu jõudma millegi huvitavani. Õnneks saame
            // kõiki neid käsitleda vaikimisi skeemiga.
        };
        return visitor.visit(tree);
    }

    // testide jaoks...
    public static int evaluate(String expr) {
        AktkLexer lexer = new AktkLexer(CharStreams.fromString(expr));
        AktkParser parser = new AktkParser(new CommonTokenStream(lexer));
        return evaluate(parser.programm());
    }

    // Ise katsetamiseks:
    public static void main(String[] args) {
        ParseTree parseTree = createParseTree("5");
        // Enne järgmist võib ka breakpoint panna ja parsepuu debuugeris vaadata.
        // Seal on palju muud infot ka, aga children võib muudkui lahti klõpsutada!
        System.out.println(evaluate(parseTree));
    }

}
