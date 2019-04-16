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
        while (peek() != TERMINATOR) {
            if (Character.isDigit(peek())) {
                return readNumber();
            } else if (Character.isWhitespace(peek())) {
                consume();
            } else if (peek() == '"') {
                return readString();
            } else if (Character.isLetter(peek()) || peek() == '_') {
                return readVariableOrKeyword();
            } else if (peek() == '(') {
                consume();
                return singleCharToken(LPAREN);
            } else if (peek() == ')') {
                consume();
                return singleCharToken(RPAREN);
            } else if (peek() == '+') {
                consume();
                return singleCharToken(PLUS);
            } else if (peek() == '-') {
                consume();
                return singleCharToken(MINUS);
            } else if (peek() == '*') {
                consume();
                return singleCharToken(TIMES);
            } else if (peek() == '/') {
                consume();
                if (peek() == '/') {
                    consume();
                    skipSingleLineComment();
                } else if (peek() == '*') {
                    consume();
                    skipMultiLineComment();
                } else {
                    return singleCharToken(DIV);
                }
            } else {
                throw new RuntimeException("Unexpected symbol");
            }
        }
        return new Token(EOF, pos, 0);
    }

    private Token singleCharToken(TokenType type) {
        return new Token(type, pos - 1, 1);
    }

    private void skipMultiLineComment() {
        do {
            while (peek() != '*') consume();
            consume();
        } while (peek() != '/');
        consume();
    }

    private void skipSingleLineComment() {
        do {
            consume();
        } while (peek() != '\n' && peek() != TERMINATOR);
    }

    private Token readVariableOrKeyword() {
        assert Character.isLetter(peek()) || peek() == '_';
        int initPos = this.pos;

        StringBuilder sb = new StringBuilder();
        sb.append(peek());
        consume();

        while ((Character.isLetter(peek())
                || Character.isDigit(peek())
                || peek() == '_')) {
            sb.append(peek());
            consume();
        }

        Token token;
        String content = sb.toString();
        switch (content) {
            case "if":
                token = new Token(IF);
                break;
            case "while":
                token = new Token(WHILE);
                break;
            case "var":
                token = new Token(VAR);
                break;
            default:
                token = new Token(VARIABLE, content);
                break;
        }
        token.setPosition(initPos, content.length());
        return token;
    }

    private Token readString() {
        assert peek() == '"';
        int initPos = this.pos;

        StringBuilder sb = new StringBuilder();
        consume();

        while (true) {
            if (peek() == '\\') {
                consume();
                switch (peek()) {
                    case 'n':
                        sb.append('\n');
                        consume();
                        break;
                    case 't':
                        sb.append('\t');
                        consume();
                        break;
                    case '"':
                        sb.append('\"');
                        consume();
                        break;
                    default:
                        throw new RuntimeException("Unknown character escape");
                }
            } else if (peek() == '"') {
                consume();
                break;
            } else {
                sb.append(peek());
                consume();
            }
        }

        return new Token(STRING, sb.toString(), initPos, this.pos - initPos);
    }

    private Token readNumber() {
        assert Character.isDigit(peek());
        int initPos = this.pos;

        int integerPart = readInteger();
        Token token;
        if (peek() == '.') {
            consume();
            int fractionalPart = readInteger();
            token = new Token(DOUBLE, Double.valueOf(integerPart + "." + fractionalPart));
        } else {
            token = new Token(INTEGER, integerPart);
        }
        token.setPosition(initPos, pos - initPos);
        return token;
    }

    private int readInteger() {
        int result = 0;

        while (Character.isDigit(peek())) {
            result = result * 10 + Integer.parseInt(String.valueOf(peek()));
            consume();
        }

        return result;
    }
}
