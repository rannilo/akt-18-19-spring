package week8.eksam;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.ExceptionErrorListener;

import static org.junit.Assert.fail;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuizGrammarTest {
	public static String lastTestDescription = "";

	@Test
	public void test1_Essee() {
		legal("--- YLDINFO ---\r\n" + 
				"pealkiri: nimetu\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"ESSEE: Miks mulle meeldib AKT?\r\n" + 
				"");
		
		illegal("--- YLDINFFO ---\r\n" + 
				"pealkiri: nimetu\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"ESSEE: Miks mulle meeldib AKT?\r\n" + 
				"");
		
	}
	@Test
	public void test2_JahEi() {
		legal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"JAH-EI: Hunt hunti ei murra\r\n" + 
				"oige: jah\r\n" + 
				"");
		
		illegal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"JAH-EI: Hunt hunti ei murra\r\n" + 
				"oige: voibolla\r\n" + 
				"");
		
	}


	@Test
	public void test3_Lyhivastusega() {
		legal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"LYHIVASTUSEGA: Millal maksad eide vaeva?\r\n" + 
				"oige: homme\r\n" + 
				"\r\n" + 
				"LYHIVASTUSEGA: Mis on Eesti pealinn?\r\n" + 
				"oige: Tallinn\r\n" + 
				"");
		
		illegal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"LYHIVASTUSEGA: Millal maksad eide vaeva?\r\n" + 
				"oige: homme\r\n" + 
				"\r\n" + 
				"LYHIVASTUSEGA: Mis on Eesti pealinn?\r\n" + 
				"oige: Tallinn\r\n" + 
				"oige: Tallinn\r\n" + 
				"");
		
		illegal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"LYHIVASTUSEGA: Millal maksad eide vaeva?\r\n" + 
				"oige: homme\r\n" + 
				"\r\n" + 
				"LYHIVASTUSEGA: Mis on Eesti pealinn?\r\n" + 
				"");
	}


	@Test
	public void test4_Arvvastusega() {
		legal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"ARVUVASTUSEGA: Mitu mulli on mullivees?\r\n" + 
				"oige: 4958 +/- 3\r\n" + 
				"ARVUVASTUSEGA: Mitu linna on Eestis?\r\n" + 
				"oige: 47\r\n" + 
				"");
		
		legal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"ARVUVASTUSEGA: Mitu linna on Eestis?\r\n" + 
				"oige: 47\r\n" + 
				"");
		
		illegal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"ARVUVASTUSEGA: Mitu mulli on mullivees?\r\n" + 
				"oige: 4958 +/- \r\n" + 
				"ARVUVASTUSEGA: Mitu linna on Eestis?\r\n" + 
				"oige: 47\r\n" + 
				"");
		
	}

	@Test
	public void test5_Valikvastusega() {
		legal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"VALIKVASTUSEGA: Kuidas sai moos kommi sisse?\r\n" + 
				"- Komm kasteti moosi sisse ja keerati pahupidi\r\n" + 
				"- Systlaga pandi\r\n" + 
				"+ See on seal koguaeg olnud\r\n" + 
				"");
		
		legal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"VALIKVASTUSEGA: Kuidas sai moos kommi sisse?\r\n" + 
				"+ See on seal koguaeg olnud\r\n" + 
				"- Komm kasteti moosi sisse ja keerati pahupidi\r\n" + 
				"- Systlaga pandi\r\n" + 
				"");
		
		legal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"VALIKVASTUSEGA: Kuidas sai moos kommi sisse?\r\n" + 
				"- Komm kasteti moosi sisse ja keerati pahupidi\r\n" + 
				"+ See on seal koguaeg olnud\r\n" + 
				"- Systlaga pandi\r\n" + 
				"");
		
		legal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"VALIKVASTUSEGA: Kuidas sai moos kommi sisse?\r\n" + 
				"+ Moos on moos on moos\r\n" + 
				"- Komm kasteti moosi sisse ja keerati pahupidi\r\n" + 
				"+ See on seal koguaeg olnud\r\n" + 
				"- Systlaga pandi\r\n" + 
				"");
		
		legal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"VALIKVASTUSEGA: Kuidas sai moos kommi sisse?\r\n" + 
				"+ See on seal koguaeg olnud\r\n" + 
				"");
		
		illegal("--- YLDINFO ---\r\n" + 
				"pealkiri: test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"VALIKVASTUSEGA: Kuidas sai moos kommi sisse?\r\n" + 
				"- Moos on moos on moos\r\n" + 
				"- Komm kasteti moosi sisse ja keerati pahupidi\r\n" + 
				"- See on seal koguaeg olnud\r\n" + 
				"- Systlaga pandi\r\n" + 
				"");
		
		
	}

	@Test
	public void test6_ErinevJarjekord() {
		legal("--- YLDINFO ---\r\n" + 
				"pealkiri: AKT test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"VALIKVASTUSEGA: Kuidas sai moos kommi sisse?\r\n" + 
				"- Komm kasteti moosi sisse ja keerati pahupidi\r\n" + 
				"- Systlaga pandi\r\n" + 
				"+ See on seal koguaeg olnud\r\n" + 
				"\r\n" + 
				"ARVUVASTUSEGA: Mitu mulli on mullivees?\r\n" + 
				"oige: 4958 +/- 3\r\n" + 
				"LYHIVASTUSEGA: Millal maksad eide vaeva?\r\n" + 
				"oige: homme\r\n" + 
				"\r\n" + 
				"JAH-EI: Hunt hunti ei murra\r\n" + 
				"oige: jah\r\n" + 
				"\r\n" + 
				"ESSEE: Miks mulle meeldib AKT?\r\n" + 
				"ARVUVASTUSEGA: Mitu linna on Eestis?\r\n" + 
				"oige: 47\r\n" + 
				"");
		
		legal(
				"--- KYSIMUSED ---\r\n" + 
				"ARVUVASTUSEGA: Mitu mulli on mullivees?\r\n" + 
				"oige: 4958 +/- 3\r\n" + 
				"LYHIVASTUSEGA: Millal maksad eide vaeva?\r\n" + 
				"oige: homme\r\n" + 
				"\r\n" + 
				"VALIKVASTUSEGA: Kuidas sai moos kommi sisse?\r\n" + 
				"- Komm kasteti moosi sisse ja keerati pahupidi\r\n" + 
				"- Systlaga pandi\r\n" + 
				"+ See on seal koguaeg olnud\r\n" + 
				"\r\n" + 
				"JAH-EI: Hunt hunti ei murra\r\n" + 
				"oige: jah\r\n" + 
				"\r\n" + 
				"ESSEE: Miks mulle meeldib AKT?\r\n" + 
				"ARVUVASTUSEGA: Mitu linna on Eestis?\r\n" + 
				"oige: 47\r\n" +
				"\r\n" + 
				"--- YLDINFO ---\r\n" + 
				"pealkiri: AKT test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				""
				);
		
		
		legal(
				"--- KYSIMUSED ---\r\n" + 
				"ARVUVASTUSEGA: Mitu mulli on mullivees?\r\n" + 
				"oige: 4958 +/- 3\r\n" + 
				"LYHIVASTUSEGA: Millal maksad eide vaeva?\r\n" + 
				"oige: homme\r\n" + 
				"\r\n" + 
				"VALIKVASTUSEGA: Kuidas sai moos kommi sisse?\r\n" + 
				"- Komm kasteti moosi sisse ja keerati pahupidi\r\n" + 
				"- Systlaga pandi\r\n" + 
				"+ See on seal koguaeg olnud\r\n" + 
				"\r\n" + 
				"JAH-EI: Hunt hunti ei murra\r\n" + 
				"oige: jah\r\n" + 
				"\r\n" + 
				"ESSEE: Miks mulle meeldib AKT?\r\n" + 
				"ARVUVASTUSEGA: Mitu linna on Eestis?\r\n" + 
				"oige: 47\r\n" +
				"\r\n" + 
				"--- YLDINFO ---\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"pealkiri: AKT test\r\n" + 
				""
				);
		
		
		illegal(
				"--- YLDINFO ---\r\n" + 
				"pealkiri: AKT test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				"\r\n" + 
				"--- KYSIMUSED ---\r\n" + 
				"ARVUVASTUSEGA: Mitu mulli on mullivees?\r\n" + 
				"oige: 4958 +/- 3\r\n" + 
				"LYHIVASTUSEGA: Millal maksad eide vaeva?\r\n" + 
				"oige: homme\r\n" + 
				"\r\n" + 
				"VALIKVASTUSEGA: Kuidas sai moos kommi sisse?\r\n" + 
				"- Komm kasteti moosi sisse ja keerati pahupidi\r\n" + 
				"- Systlaga pandi\r\n" + 
				"+ See on seal koguaeg olnud\r\n" + 
				"\r\n" + 
				"JAH-EI: Hunt hunti ei murra\r\n" + 
				"oige: jah\r\n" + 
				"\r\n" + 
				"ESSEE: Miks mulle meeldib AKT?\r\n" + 
				"ARVUVASTUSEGA: Mitu linna on Eestis?\r\n" + 
				"oige: 47\r\n" +
				"\r\n" + 
				"--- YLDINFO ---\r\n" + 
				"pealkiri: AKT test\r\n" + 
				"ajapiirang: 105 min\r\n" + 
				""
				);
		
		
	}


    private void legal(String program) {
        check(program, true);
    }

    private void illegal(String program) {
        check(program, false);
    }

    private void check(String program, boolean legal) {
        try {
            System.err.close();
        } catch (Throwable t) {

        }



        boolean parses = true;
        try {
            parseWithExceptions(program);
        } catch (Throwable e) {
            parses = false;
        }

        lastTestDescription = "Katsetan sellise "
                + (legal ? "legaalse" : "mittelegaalse")
                + " sisendiga:\n\n>"
                + program.replaceAll("\\r\\n", "\n").replaceAll("\n", "\n>");


        if (legal) {
            if (!parses) {
                fail("sinu grammatika ei aktsepteerinud seda");
            }
        }
        else {
            if (parses) {
                fail("sinu grammatika aktsepteeris seda");
            }
        }
    }

	private static final ExceptionErrorListener listener = new ExceptionErrorListener();

	private static ParseTree parseWithExceptions(String program) {
        CharStream input = CharStreams.fromString(program);
        QuizLexer lexer = new QuizLexer(input);
        lexer.addErrorListener(listener);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        QuizParser parser = new QuizParser(tokens);

        parser.addErrorListener(listener);

        ParseTree parseTree = parser.quiz();
        if (parseTree == null
                || parseTree.getChildCount() == 0
                || parser.getNumberOfSyntaxErrors() != 0
                ) {
            throw new RuntimeException("Problem with parsing");
        }
        
        if (tokens.LA(1) != -1) {
        	throw new RuntimeException("Some tokens left after parsing");
        }
        
        return parseTree;
    }
}
