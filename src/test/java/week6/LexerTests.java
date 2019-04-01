package week6;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static week6.TokenType.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LexerTests {
    public static String lastTestDescription = "";

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

    @Test
    public void test01_int() {
        checkBasic("7 ",
                new Token(INTEGER, 7),
                new Token(EOF));
        checkBasic(" 35",
                new Token(INTEGER, 35),
                new Token(EOF));
    }

    @Test
    public void test02_NumsTypes() {
        checkBasic("3",
                new Token(INTEGER, 3),
                new Token(EOF));

        checkBasic("0",
                new Token(INTEGER, 0),
                new Token(EOF));

        checkBasic("13.45",
                new Token(DOUBLE, 13.45),
                new Token(EOF));

        checkBasic("13.0",
                new Token(DOUBLE, 13.0),
                new Token(EOF));

        checkBasic("0.0",
                new Token(DOUBLE, 0.0),
                new Token(EOF));

        checkBasic("0.5",
                new Token(DOUBLE, 0.5),
                new Token(EOF));

    }

    @Test
    public void test03_Expressions() {
        checkBasic("3+4",
                new Token(INTEGER, 3),
                new Token(PLUS),
                new Token(INTEGER, 4),
                new Token(EOF));

        checkBasic("3- 4",
                new Token(INTEGER, 3),
                new Token(MINUS),
                new Token(INTEGER, 4),
                new Token(EOF));

        checkBasic("13 *488",
                new Token(INTEGER, 13),
                new Token(TIMES),
                new Token(INTEGER, 488),
                new Token(EOF));

        checkBasic("78/ 2",
                new Token(INTEGER, 78),
                new Token(DIV),
                new Token(INTEGER, 2),
                new Token(EOF));

        checkBasic("x /3",
                new Token(VARIABLE, "x"),
                new Token(DIV),
                new Token(INTEGER, 3),
                new Token(EOF));

        checkBasic("78 / (13*488)",
                new Token(INTEGER, 78),
                new Token(DIV),
                new Token(LPAREN),
                new Token(INTEGER, 13),
                new Token(TIMES),
                new Token(INTEGER, 488),
                new Token(RPAREN),
                new Token(EOF));
    }

    @Test
    public void test04_varsKeywords() {
        checkBasic("if",
                new Token(IF),
                new Token(EOF));

        checkBasic("ifs",
                new Token(VARIABLE, "ifs"),
                new Token(EOF));
        
        checkBasic("while",
                new Token(WHILE),
                new Token(EOF));

        checkBasic("whilewhile",
                new Token(VARIABLE, "whilewhile"),
                new Token(EOF));
        
        checkBasic("var",
                new Token(VAR),
                new Token(EOF));

        checkBasic("varikatus",
                new Token(VARIABLE, "varikatus"),
                new Token(EOF));
    }


    @Test
    public void test05_varsNumsKeywords() {
        checkBasic("if while varu_2",
                new Token(IF),
                new Token(WHILE),
                new Token(VARIABLE, "varu_2"),
                new Token(EOF));

        checkBasic("12 if 4.5 ifs",
                new Token(INTEGER, 12),
                new Token(IF),
                new Token(DOUBLE, 4.5),
                new Token(VARIABLE, "ifs"),
                new Token(EOF));

        checkBasic("12\nif\n4.5 ifs",
                new Token(INTEGER, 12),
                new Token(IF),
                new Token(DOUBLE, 4.5),
                new Token(VARIABLE, "ifs"),
                new Token(EOF));
    }

    @Test
    public void test06_simpleStrings() {
        checkBasic("\"plrrrahh\"",
                new Token(STRING, "plrrrahh"),
                new Token(EOF));
        checkBasic("\"\"",
                new Token(STRING, ""),
                new Token(EOF));
        checkBasic("\"eGo\"",
                new Token(STRING, "eGo"),
                new Token(EOF));
        checkBasic("\".\"",
                new Token(STRING, "."),
                new Token(EOF));
    }

    @Test
    public void test07_Comments() {

        checkBasic("x 3 //\"plahh\"",
                new Token(VARIABLE, "x"),
                new Token(INTEGER, 3),
                new Token(EOF));

        checkBasic("x 3 //\"plahh\"\ntere",
                new Token(VARIABLE, "x"),
                new Token(INTEGER, 3),
                new Token(VARIABLE, "tere"),
                new Token(EOF));

        checkBasic("x /*4 5 6*/ 3",
                new Token(VARIABLE, "x"),
                new Token(INTEGER, 3),
                new Token(EOF));

        checkBasic("x /*4 /* * /5 6*/ 3",
                new Token(VARIABLE, "x"),
                new Token(INTEGER, 3),
                new Token(EOF));

        //checkBasic("x  * /*/5 6*/ 3",
        checkBasic("x  * /*5 6*/ 3",
                new Token(VARIABLE, "x"),
                new Token(TIMES),
                new Token(INTEGER, 3),
                new Token(EOF));
    }


    @Test
    public void test08_escapedSymbols() {
        checkBasic("\"plrah\\nh\"",
                new Token(STRING, "plrah\nh"),
                new Token(EOF));

        checkBasic("\"plrah\\\"h\"",
                new Token(STRING, "plrah\"h"),
                new Token(EOF));


        checkBasic(" 3.14-a +\"ka\\nla\"",
                new Token(DOUBLE, 3.14),
                new Token(MINUS),
                new Token(VARIABLE, "a"),
                new Token(PLUS),
                new Token(STRING, "ka\nla"),
                new Token(EOF)
        );

    }

    @Test
    public void test09_Positions() {
        checkPositioned(" 3.14-a +\"ka\\nla\"",
                new Token(DOUBLE, 3.14, 1, 4),
                new Token(MINUS, 5, 1),
                new Token(VARIABLE, "a", 6, 1),
                new Token(PLUS, 8, 1),
                new Token(STRING, "ka\nla", 9, 8),
                new Token(EOF, 17, 0)
        );

    }

    @Test
    public void test10_readOnlyNeeded() {
        LinkedHashMap<String, Token> pieces = new LinkedHashMap<>();
        pieces.put("   \"xyz\" /* kalapala*/", new Token(STRING, "xyz"));
        pieces.put("3 a", new Token(INTEGER, 3));
        pieces.put("bc\0", new Token(VARIABLE, "abc"));
        checkReadNextToken(pieces);
    }


    private void checkBasic(String input, Token... expectedTokensArray) {
        lastTestDescription = "Sisend:\n>"
                + input.replaceAll("\\r\\n", "\n").replaceAll("\\n", "\n>");

        List<Token> expectedTokens = Arrays.asList(expectedTokensArray);
        Lexer lexer = new Lexer(new StringReader(input));
        List<Token> actualTokens = lexer.readAllTokens();

        if (!expectedTokens.equals(actualTokens)) {
            fail("Ootasin tulemuseks sellist lekseemide jada:\n"
                    + formatTokens(expectedTokens)
                    + "aga sain:\n"
                    + formatTokens(actualTokens));
        }
    }

    private void checkPositioned(String input, Token... expectedTokensArray) {
        lastTestDescription = "Sisend:\n>"
                + input.replaceAll("\\r\\n", "\n").replaceAll("\\n", "\n>");

        List<Token> expectedTokens = Arrays.asList(expectedTokensArray);
        Lexer lexer = new Lexer(new StringReader(input));
        List<Token> actualTokens = lexer.readAllTokens();
        assertTrue("Offset puudu!", actualTokens.stream().noneMatch(Token::noOffset));

        if (!expectedTokens.equals(actualTokens)) {
            fail("Ootasin tulemuseks sellist lekseemide jada:\n"
                    + formatTokens(expectedTokens)
                    + "aga sain:\n"
                    + formatTokens(actualTokens));
        }
    }

    private void checkReadNextToken(LinkedHashMap<String, Token> pieces) {
        StringBuilder wholeInput = new StringBuilder();
        for (String key : pieces.keySet()) {
            wholeInput.append(key.replaceAll("\\u0000", ""));
        }

        lastTestDescription = "Kogu sisend:\n> " + wholeInput.toString().replaceAll("\\n", "\n>");


        StringBuilder providedInput = new StringBuilder();

        try {
            PipedReader reader = new PipedReader();
            PipedWriter writer = new PipedWriter(reader);
            Lexer lexer = new Lexer(reader);

            for (Map.Entry<String, Token> entry : pieces.entrySet()) {
                String piece = entry.getKey();
                if (piece.endsWith("\0")) {
                    writer.write(piece.substring(0, piece.length() - 1));
                    writer.flush();
                    writer.close();
                } else {
                    writer.write(piece);
                    writer.flush();
                }

                providedInput.append(piece);

                Token expectedToken = entry.getValue();

                try {
                    Token actualToken = lexer.readNextToken();

                    if (!actualToken.equals(expectedToken)) {
                        fail("Ootasin token-it " + expectedToken + ", aga tuli " + actualToken);
                    }
                } catch (Error e) {
                    fail("Kui sisendisse oli antud '" + providedInput.toString().replaceAll("\\u0000", "<sisendi lõpp>")
                            + "', siis järgmise getNextToken()-iga tekkis probleem: "
                            + e.getMessage());
                }
            }

            if (!lexer.readNextToken().equals(new Token(EOF))) {
                fail("Eeldasin, et peale sisendi lõppu annab getNextToken EOF-i");
            }


        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String formatTokens(List<Token> tokens) {
        StringBuilder sb = new StringBuilder("");

        for (Token token : tokens) {
            sb.append(">").append(token).append("\n");
        }

        return sb.toString();
    }


}
