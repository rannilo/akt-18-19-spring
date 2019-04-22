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
public class WikiGrammarTest {
	public static String lastTestDescription = "";

	@Test
	public void test1_Pealkiri() {
		legal("Pealkiri\n" + 
				"-------\n" + 
				"Esimene paragrahv\n");
		
		illegal("Esimene paragrahv\n");
		
		illegal("Pealkiri\n" + 
				"Esimene paragrahv\n");
		
		illegal("Pealkiri\n" + 
				"---\n" + 
				"Esimene paragrahv\n");
		
		
		illegal("Pealkiri\n" + 
				"----- -----\n" + 
				"Esimene paragrahv\n");
		
	}
	@Test
	public void test2_Paragrahv() {
		legal("Pealkiri\n" + 
				"-------\n" + 
				"Esimene paragrahv\n");
		
		illegal("Pealkiri\n" + 
				"---\n" + 
				"Esimene paragrahv\n");
		
		illegal("Pealkiri\n" + 
				"Esimene paragrahv\n");
	}


	@Test
	public void test3_Kood() {
		legal("Pealkiri\n" + 
				"-------\n" + 
				"> esimene koodirida\n" +
				"> teine koodirida\n");
		
		illegal("Pealkiri\n" + 
				"-------\n" + 
				">esimene koodirida\n" +
				">teine koodirida\n");
	}


	@Test
	public void test4_LihtneLoetelu() {
		legal("Pealkiri\n" + 
				"-------\n" + 
				"* esimene loetelu punkt\n" +
				"* teine loetelu punkt\n");
		
		illegal("Pealkiri\n" + 
				"-------\n" + 
				"*esimene loetelu punkt\n" +
				"*teine loetelu punkt\n");
		
		illegal("Pealkiri\n" + 
				"-------\n" + 
				" * esimene loetelu punkt\n" +
				" * teine loetelu punkt\n");
		
	}

	@Test
	public void test5_MitmetasemelineLoetelu() {
		legal("Pealkiri\n" + 
				"-------\n" + 
				"* esimene loetelu punkt\n" +
				"** blaaa\n" +
				"** blaaa\n" +
				"*** blaaa\n" +
				"*** blaaa\n" +
				"*** blaaa\n" +
				"** blaaa\n" +
				"* teine loetelu punkt\n");
		
		illegal("Pealkiri\n" + 
				"-------\n" + 
				"** kohe teine tase\n" +
				"** blaaa\n" +
				"* teine loetelu punkt\n");
		
		
	}

	@Test
	public void test6_Varia() {
		legal("See siin on pealkiri\n" + 
				"----------------------\n" + 
				"Esimese paragrahvi esimene rida.\n" + 
				"Esimese paragrahvi teine rida.\n" + 
				"\n" + 
				"Teine paragrahv\n" + 
				"\n" + 
				"* See siin on esimese taseme loetelupunkt.\n" + 
				"* Uskumatu, aga see on samuti esimese taseme loetelupunkt.\n" + 
				"** teise taseme loetelupunkt ...\n" + 
				"* esimese taseme loetelupunkt ...\n" + 
				"** teise taseme loetelupunkt ...\n" + 
				"** teise taseme loetelupunkt ...\n" + 
				"*** kolmanda taseme loetelupunkt ...\n" + 
				"*** kolmanda taseme loetelupunkt ...\n" + 
				"\n" + 
				"> koodiploki esimene rida\n" + 
				"> koodiploki teine rida\n");
		
		
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
                + " programmiga:\n\n>"
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
        WikiLexer lexer = new WikiLexer(input);
        lexer.addErrorListener(listener);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        WikiParser parser = new WikiParser(tokens);

        parser.addErrorListener(listener);

        ParseTree parseTree = parser.wiki();
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
