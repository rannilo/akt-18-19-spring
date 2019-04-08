package week7.parsers.mini;

import week7.parsers.Node;
import week7.parsers.Parser;

// S → ABAR
// A → a | aAb
// B → bb | BbSb
// R → +S | ε
public class AbbaParser extends Parser {

    public AbbaParser(String input) {
        super(input);
    }

    // S → ABAR
    protected Node s() {
        throw new UnsupportedOperationException();
    }

    public static Node parse(String src) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        AbbaParser parser = new AbbaParser("abba");
        parser.testParser();
    }

}
