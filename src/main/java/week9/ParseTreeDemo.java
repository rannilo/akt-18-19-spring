package week9;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import week8.AktkBaseVisitor;
import week8.AktkLexer;
import week8.AktkParser;
import week9.pohiosa.LoogikaLexer;

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
            public Integer visitArvuliteraal(AktkParser.ArvuliteraalContext ctx) {
                String text = ctx.getText();
                return Integer.parseInt(text);
            }

            // Järgmise juhtumi mõistmiseks otsi grammatikast üles
            // sildid KorrutamineJagamine ja LiitmineLahutamine --
            // loodetavasti on siis arusaadav, miks siin just nii toimitakse.
            @Override
            public Integer visitKorrutamineJagamine(AktkParser.KorrutamineJagamineContext ctx) {
                return binaarneOperatsioon(ctx);
            }

            @Override
            public Integer visitLiitmineLahutamine(AktkParser.LiitmineLahutamineContext ctx) {
                return binaarneOperatsioon(ctx);
            }

            private Integer binaarneOperatsioon(ParserRuleContext ctx) {
                ParseTree left = ctx.getChild(0);
                String op = ctx.getChild(1).getText();
                ParseTree right = ctx.getChild(2);

                Integer leftValue = visit(left);
                Integer rightValue = visit(right);
                switch (op){
                    case "+":
                        return leftValue + rightValue;
                    case "-":
                        return leftValue - rightValue;
                    case "*":
                        return leftValue * rightValue;
                    case "/":
                        return leftValue / rightValue;
                    default:
                        throw new RuntimeException("Tundmatu operaator: " + op);

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
        ParseTree parseTree = createParseTree("5 + 5 * 4 / 2 - 10");
        // Enne järgmist võib ka breakpoint panna ja parsepuu debuugeris vaadata.
        // Seal on palju muud infot ka, aga children võib muudkui lahti klõpsutada!
        System.out.println(evaluate(parseTree));
    }

}
