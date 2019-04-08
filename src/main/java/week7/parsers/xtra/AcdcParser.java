package week7.parsers.xtra;

import week7.parsers.Node;
import week7.parsers.Parser;

// S → aSdS | ε | CS
// C → c | C/
public class AcdcParser extends Parser {

    public AcdcParser(String input) {
        super(input);
    }

    // S → aSdS | ε | CS
    protected Node s() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Parser p = new AcdcParser("ac/dc");
        p.testParser();
    }
}
