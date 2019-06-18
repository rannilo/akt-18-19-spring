package week7;

import week6.Token;

import java.io.*;
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

    public Lexer(String input) {
        this.reader = new StringReader(input);
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
            returnToken = new Token(TIMES, pos, 1);
        } else if (current == '/') {
            returnToken = new Token(DIV, pos, 1);
        } else if (Character.isDigit(current)) {
            return readDigit();
        } else if (Character.isLetter(current)) {
            return readVar();
        } else if (current == TERMINATOR) {
            return new Token(EOF, pos, 0);
        }
        consume();
        return returnToken;
    }


    private void skipWhitespace() {
        while (Character.toString(peek()).matches("\\s")) consume();
    }

    private Token readVar() {
        final int START_POS = pos;
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(peek())) {
            sb.append(peek());
            consume();
        }
        return new Token(VARIABLE, sb.toString(), START_POS, pos - START_POS);
    }

    private Token readDigit() {
        final int START_POS = pos;
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(peek())) {
            sb.append(peek());
            consume();
        }
        return new Token(INTEGER, Integer.parseInt(sb.toString()), START_POS, pos - START_POS);
    }
}
