package week7.parsers.mini;

import week7.parsers.Node;
import week7.parsers.Parser;

// S → AkT
// A -> aA | ε
// T -> t | tSp | Tt
// R -> tR | ε
public class AktParser extends Parser {

    public AktParser(String input) {
        super(input);
    }

    // S → AkT
    protected Node s() {
        throw new UnsupportedOperationException();
    }

    public static Node parse(String src) {
        throw new UnsupportedOperationException();
    }

}

