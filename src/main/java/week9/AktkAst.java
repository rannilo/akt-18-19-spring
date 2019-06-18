package week9;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import week8.AktkBaseVisitor;
import week8.AktkLexer;
import week8.AktkParser;
import week8.AktkParser.*;
import week9.ast.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class AktkAst {

    // Ise testimiseks soovitame kasutada selline meetod: sample.aktk failis sisend muuta.
    // Kui testid sinna kopeerida, siis äkki võtab IDE escape sümbolid ära ja on selgem,
    // milline see kood tegelikult välja näeb.
    public static void main(String[] args) throws IOException {
        //AktkLexer lexer = new AktkLexer(CharStreams.fromFileName("sample.aktk"));
        AktkLexer lexer = new AktkLexer(CharStreams.fromString("var nimi : tyyp"));
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


    private static AstNode parseTreeToAst(ParseTree tree) {
        AktkAstStatementVisitor statementVisitor = new AktkAstStatementVisitor();
        return statementVisitor.visit(tree);
    }

// Eraldi Expression ja Statement visitoride tegemine võimaldab kasutada eri
// tagastustüüpe, et ei peaks avaldiste AST-i loomisel pidevalt AstNode'sid
// Expression'iteks cast'ima

    private static class AktkAstExpressionVisitor extends AktkBaseVisitor<Expression> {
        @Override
        public Expression visitArvuliteraal(ArvuliteraalContext ctx) {
            return new IntegerLiteral(Integer.parseInt(ctx.getText()));
        }

        @Override
        public Expression visitSoneliteraal(SoneliteraalContext ctx) {
            // Arvesta, et sõneliteraalil on ümber jutumärgid, mis ei kuulu sõne sisu hulka
            return new StringLiteral(ctx.getText().replaceAll("\"", ""));
        }

        @Override
        public Expression visitMuutujaNimi(MuutujaNimiContext context){
            return new Variable(context.getText());
        }

        @Override
        public Expression visitSuluavaldis(SuluavaldisContext ctx) {
            // Selle tipu alluvad on alustav sulg, avaldis ja lõpetav sulg
            // NB! Alluvate nummerdamine algab 0-st
            // Töötleme rekursiivselt sulgude sees oleva avaldise ja tagastame selle
            return visit(ctx.getChild(1));
        }

        @Override
        public Expression visitFunktsiooniValjakutse(FunktsiooniValjakutseContext ctx) {
            // NB! Siin tuleb kontrollida, kuidas näeb välja
            //   - ilma argumentideta
            //   - ühe argumendiga
            //   - mitme argumendiga
            // funktsiooni väljakutse parse-puu
            String nimi = ctx.Nimi().getText();
            List<AvaldisContext> avaldised = ctx.avaldis();
            List<Expression> expressions = new ArrayList<>();
            for(AvaldisContext avaldis: avaldised){
                expressions.add(visit(avaldis));
            }
            FunctionCall functionCall = new FunctionCall(nimi, expressions);
            return functionCall;
        }

        @Override
        public Expression visitKorrutamineJagamine(KorrutamineJagamineContext ctx) {
            return binaarneOperatsioon(ctx);
        }

        @Override
        public Expression visitLiitmineLahutamine(LiitmineLahutamineContext ctx) {
            return binaarneOperatsioon(ctx);
        }

        @Override
        public Expression visitVordlemine(VordlemineContext ctx) {
            return binaarneOperatsioon(ctx);
        }

        private Expression binaarneOperatsioon(ParserRuleContext ctx) {
            // Kõik binaarsed operatsioonid saan käsitleda korraga
            String operaator = ctx.getChild(1).getText();
            Expression vasakArgument = visit(ctx.getChild(0));
            Expression paremArgument = visit(ctx.getChild(2));
            return new FunctionCall(operaator, Arrays.asList(vasakArgument, paremArgument));
        }

        @Override
        public Expression visitUnaarneMiinus(UnaarneMiinusContext context){
            return new FunctionCall("-", Collections.singletonList(visit(context.avaldis2())));
        }
    }

    private static class AktkAstStatementVisitor extends AktkBaseVisitor<Statement> {
        private final AktkAstExpressionVisitor expressionVisitor = new AktkAstExpressionVisitor();

        @Override
        public Statement visitAvaldis(AvaldisContext ctx) {
            // Kui lause koosneb avaldisest, siis selleks, et temast saaks ikkagi lause,
            // tuleb ta avaldise visitoriga töödelda ja pakendada ExpressionStatement'i sisse
            Expression avaldis = expressionVisitor.visit(ctx.getChild(0));
            return new ExpressionStatement(avaldis);
        }

        @Override
        public Statement visitMuutujaDeklaratsioon(MuutujaDeklaratsioonContext ctx) {
            // Muutuja deklaratsiooni esimene alluv (st. alluv 0) on võtmesõna "var",
            // teine alluv on muutuja nimi

            // Algväärtus võib olla, aga ei pruugi.
            // Kontrolli ANTLRi IntelliJ pluginaga järgi, mitu alluvat
            // on muutuja deklaratsioonil, millel on algväärtus ja mitu
            // alluvat on sellel, millel algväärtust pole.
            String variableName;
            String type = null;
            Expression initializer = null;
            List<TerminalNode> nimed = ctx.Nimi();
            variableName = nimed.get(0).getText();
            if(nimed.size() > 1) type = nimed.get(1).getText();
            if(ctx.avaldis() != null) initializer = expressionVisitor.visit(ctx.avaldis());
            return new VariableDeclaration(variableName, type, initializer);
        }

        @Override
        public Statement visitLause(LauseContext ctx) {
            // Grammatikast on näha, et lause võib olla ühe alluvaga,
            // (nt. ifLause, whileLause), mis on käsitletud mujal
            if (ctx.getChildCount() == 1) {
                return visit(ctx.getChild(0));
            }
            // ... aga lause võib olla ka loogelistes sulgudes olev lausete jada
            else {
                assert ctx.getChildCount() == 3;
                return visit(ctx.getChild(1));
            }
        }

        @Override
        public Statement visitLauseteJada(LauseteJadaContext ctx) {
            // Siin on tähtis järgi uurida, kuidas näib välja mitme lausega
            // lauseteJada parse-puu.
            List<LauseContext> laused = ctx.lause();
            List<Statement> statements = new ArrayList<>();
            for(LauseContext lause: laused){
                statements.add(visit(lause));
            }
            Block block = new Block(statements);
            return block;
        }

        @Override
        public Statement visitOmistamine(OmistamineContext context){
            String nimi = context.Nimi().getText();
            Expression expression = expressionVisitor.visit(context.avaldis());
            return new Assignment(nimi, expression);
        }

        @Override
        public Statement visitFunktsiooniDefinitsioon(FunktsiooniDefinitsioonContext context){
            String nimi = context.funktsiooninimi.getText();
            String tagastustyyp = context.tagastustyyp == null ? null : context.tagastustyyp.getText();
            ArrayList<FunctionParameter> parameters = new ArrayList<>();
            for(int i = 0; i<context.parameetriNimi.size(); i++){
                String parameetriNimi = context.parameetriNimi.get(i).getText();
                String parameetriTyyp = context.parameetriTyyp.get(i).getText();
                parameters.add(new FunctionParameter(parameetriNimi, parameetriTyyp));
            }
            Block keha = (Block)visitLauseteJada(context.lauseteJada());
            return new FunctionDefinition(nimi, parameters, tagastustyyp, keha);
        }

        @Override
        public Statement visitTagastuslause(TagastuslauseContext context){
            return new ReturnStatement(expressionVisitor.visit(context.avaldis()));
        }

        @Override
        public Statement visitIfLause(IfLauseContext context){
            return new IfStatement(expressionVisitor.visit(context.avaldis()), visit(context.lause(0)), visit(context.lause(1)));
        }

        @Override
        public Statement visitWhileLause(WhileLauseContext context){
            return new WhileStatement(expressionVisitor.visit(context.avaldis()), visit(context.lause()));
        }
    }
}
