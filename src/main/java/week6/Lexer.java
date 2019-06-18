package week6;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static week6.TokenType.*;

public class Lexer {

    // Siin on meie poolt mall ette antud, millega saad kasutada praktikumist tuttavad meetodid.
    // Selle malli kasutamine ei ole kohustuslik, aga sisend antakse ette Reader liidese kaudu
    // ja viimase testi läbimiseks ei tohi sisendis liiga palju ette vaadata.

    private static final char TERMINATOR = '\0';
    private final Reader reader;
    private Character current;
    private int pos = 0;

    public Lexer(Reader input) {
        this.reader = input;
    }

    // See teeb meil testimise mugavamaks:
    public List<Token> readAllTokens() {
        List<Token> tokens = new ArrayList<>();

        Token token;
        do {
            token = this.readNextToken();
            tokens.add(token);
        } while (token.getType() != EOF);

        return tokens;
    }

    // Saame kasutada näiteks järgmiselt:
    public static void main(String[] args) throws IOException {
        System.out.println(new String(Files.readAllBytes(Paths.get("input3.txt"))));
        System.out.println(new Lexer(new FileReader("input3.txt")).readAllTokens());
    }


    // Siin on abimeetodid, mida soovitame kasutada.
    // Consume on sisuliselt nagu pos++ kalaparseri näites ehk läheb järgmise tähe juurde.
    private void consume() {
        if (current == TERMINATOR) {
            throw new RuntimeException("Reading passed terminator!");
        }
        read();
        pos++;
    }

    // Peek on see, mis tagastab hetkel vaadeldavat tähte.
    // (Kõige algul võib see täht olla puudu.)
    private char peek() {
        if (current == null) read();
        return current;
    }

    // See on pigem abimeetod, mis teostab tegeliku sisendist lugemist.
    // Meie enda lahenduses me seda otse ei kasuta. Kutsume ainult peek ja consume.
    private void read() {
        try {
            int i = reader.read();
            current = (i == -1) ? TERMINATOR : (char) i;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    /**
     * See on nüüd see oluline meetod, mida peab ise implementeerima!
     */
    public Token readNextToken() {
        skipWhitespace();
        while(peek() == '/'){
            consume();
            if (peek() == '*' || peek() == '/'){
                skipComments();
                skipWhitespace();
            }
            else{
                return new Token(DIV, pos-1, 1);
            }
        }
        Token returnToken = new Token(EOF, pos, 0);
        if (current == '(') {
            returnToken = new Token(LPAREN, pos, 1);
        } else if (current == ')') {
            returnToken = new Token(RPAREN, pos, 1);
        } else if (current == '+') {
            returnToken = new Token(PLUS, pos, 1);
        } else if (current == '-') {
            returnToken = new Token(MINUS, pos, 1);
        } else if (current == '*') {
            returnToken = new Token(TIMES, pos, 5);
        }  else if (current == '"') {
            return readString();
        } else if (Character.isDigit(current)) {
            return readDigit();
        } else if (Character.isLetter(current)) {
            return readVarOrKeyword();
        } else if (current == TERMINATOR) {
            return new Token(EOF, pos, 0);
        }
        consume();
        return returnToken;
    }

    private void skipComments() {
        switch (peek()) {
            case '*':
                while (true) {
                    consume();
                    if (peek() == '*') {
                        consume();
                        if (peek() == '/') {
                            consume();
                            return;
                        }
                    }
                }
            case '/':
                while (true) {
                    consume();
                    if (peek() == TERMINATOR) return;
                    if (peek() == '\n') {
                        consume();
                        return;
                    }
                }
        }
    }

    private Token readString() {
        final int START_POS = pos;
        consume(); //consume'n algava jutumärgi
        StringBuilder sb = new StringBuilder();
        while (true) {
            switch (peek()) {
                case '\\':
                    consume();
                    switch (peek()){
                        case 'n':
                            sb.append('\n');
                            break;
                        case '\"':
                            sb.append('\"');
                            break;
                        case 't':
                            sb.append('\t');
                        case '\\':
                            sb.append('\\');
                    }
                    consume();
                    break;
                case '"':
                    consume();
                    return new Token(STRING, sb.toString(), START_POS, pos-START_POS);
                default:
                    sb.append(peek());
                    consume();
            }
        }
    }

    private void skipWhitespace() {
        while (Character.toString(peek()).matches("\\s")) consume();
    }

    private Token readVarOrKeyword() {
        final int START_POS = pos;
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(peek())) {
            sb.append(peek());
            consume();
        }
        if (peek() == '_') {
            sb.append(peek());
            consume();
            while (Character.isDigit(peek())) {
                sb.append(peek());
                consume();
            }
            return new Token(VARIABLE, sb.toString(), START_POS, pos-START_POS);
        }
        switch (sb.toString()) {
            case "while":
                return new Token(WHILE, START_POS, 5);
            case "if":
                return new Token(IF, START_POS, 2);
            case "var":
                return new Token(VAR, START_POS, 2);
            default:
                return new Token(VARIABLE, sb.toString(), START_POS, pos-START_POS);
        }
    }

    private Token readDigit() {
        final int START_POS = pos;
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(peek())) {
            sb.append(peek());
            consume();
        }
        if (peek() == '.') {
            sb.append(peek());
            consume();
            while (Character.isDigit(peek())) {
                sb.append(peek());
                consume();
            }
            return new Token(DOUBLE, Double.parseDouble(sb.toString()), START_POS, pos-START_POS);
        }
        return new Token(INTEGER, Integer.parseInt(sb.toString()), START_POS, pos-START_POS);
    }
}
