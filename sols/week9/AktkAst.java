package week9;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import week8.AktkBaseVisitor;
import week8.AktkLexer;
import week8.AktkParser;
import week8.AktkParser.*;
import week9.ast.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
			return new StringLiteral(ctx.getText().substring(1, ctx.getText().length()-1));
		}

		@Override
		public Expression visitMuutujaNimi(MuutujaNimiContext ctx) {
			return new Variable(ctx.getText());
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
			String funktsiooniNimi = ctx.getChild(0).getText();

			List<Expression> argumendid = new ArrayList<Expression>();

			if (ctx.getChildCount() > 3) {
				// Argumentavaldised on paarisarvulise indeksiga lapsed
				for (int i=2; i < ctx.getChildCount(); i += 2) {
					argumendid.add(visit(ctx.getChild(i)));
				}
			}

			return new FunctionCall(funktsiooniNimi, argumendid);
		}

		@Override
		public Expression visitUnaarneMiinus(UnaarneMiinusContext ctx) {
			// võime eeldada, et miinuse argument on avaldis
			Expression arg = visit(ctx.getChild(1));
			return new FunctionCall("-", Arrays.asList(arg));
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
		public Statement visitFunktsiooniDefinitsioon(FunktsiooniDefinitsioonContext ctx) {
			String name = ctx.getChild(1).getText();
			Block body = (Block) visit(ctx.getChild(ctx.getChildCount()-2));
			List<FunctionParameter> params = new ArrayList<>();

			String returnType = null;
			if (ctx.getChild(ctx.getChildCount() - 5).getText().equals("->"))
				returnType = ctx.getChild(ctx.getChildCount() - 4).getText();

			for (int i=3; i < ctx.getChildCount() - (returnType != null ? 6 : 4); i += 4) {
				FunctionParameter param = new FunctionParameter(ctx.getChild(i).getText(), ctx.getChild(i+2).getText());
				params.add(param);
			}
			FunctionDefinition funDef = new FunctionDefinition(name, params, returnType, body);
			return funDef;
		}

		@Override
		public Statement visitTagastuslause(TagastuslauseContext ctx) {
			return new ReturnStatement(expressionVisitor.visit(ctx.getChild(1)));
		}

		@Override
		public Statement visitMuutujaDeklaratsioon(MuutujaDeklaratsioonContext ctx) {
			// Muutuja deklaratsiooni esimene alluv (st. alluv 0) on võtmesõna "var",
			// teine alluv on muutuja nimi
			String muutujaNimi = ctx.getChild(1).getText();

			// Algväärtus võib olla, aga ei pruugi.
			// Kontrolli ANTLRi IntelliJ pluginaga järgi, mitu alluvat
			// on muutuja deklaratsioonil, millel on algväärtus ja mitu
			// alluvat on sellel, millel algväärtust pole.
			Expression algVäärtustusAvaldis = null;
			String tüüp = null;
			if (ctx.getChildCount() == 4) {
				if (ctx.getChild(2).getText().equals("=")) {
					algVäärtustusAvaldis = expressionVisitor.visit(ctx.getChild(3));
				} else {
					tüüp = ctx.getChild(3).getText();
				}
			}
			else if (ctx.getChildCount() == 6) {
				tüüp = ctx.getChild(3).getText();
				algVäärtustusAvaldis = expressionVisitor.visit(ctx.getChild(5));
			}

			VariableDeclaration decl = new VariableDeclaration(muutujaNimi, tüüp, algVäärtustusAvaldis);
			return decl;
		}

		@Override
		public Statement visitOmistamine(OmistamineContext ctx) {
			String muutujaNimi = ctx.getChild(0).getText();
			Expression avaldis = expressionVisitor.visit(ctx.getChild(2));
			return new Assignment(muutujaNimi, avaldis);
		}

		@Override
		public Statement visitWhileLause(WhileLauseContext ctx) {
			Expression tingimus = expressionVisitor.visit(ctx.getChild(1));
			Statement keha = visit(ctx.getChild(3));
			return new WhileStatement(tingimus, keha);
		}

		@Override
		public Statement visitIfLause(IfLauseContext ctx) {
			Expression tingimus = expressionVisitor.visit(ctx.getChild(1));
			Statement thenHaru = visit(ctx.getChild(3));
			Statement elseHaru = visit(ctx.getChild(5));

			return new IfStatement(tingimus, thenHaru, elseHaru);
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

			List<Statement> laused = new ArrayList<Statement>();
			// Nagu näha, on alluvate jadas vaheldumis laused ja semikoolonid.
			// Lausete läbikäimiseks võtan ette kõik paarisarvuliste indeksitega alluvad:
			for (int i=0; i < ctx.getChildCount(); i += 2) {
				Statement lause = visit(ctx.getChild(i));
				laused.add(lause);
			}

			return new Block(laused);
		}
	}
}
