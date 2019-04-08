package week7.parsers.mini;

import week7.parsers.Node;
import week7.parsers.Parser;


// S → AB
// A → boA | baA | ε
// B → Blo | Bbi | rat
public class BoratParser extends Parser {

    public BoratParser(String input) {
        super(input);
    }

    // S → AB
    protected Node s() {
        throw new UnsupportedOperationException();
    }

    public static Node parse(String src) {
        BoratParser parser = new BoratParser(src);
        return parser.parse();
    }

}

