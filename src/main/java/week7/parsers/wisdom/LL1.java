package week7.parsers.wisdom;

import week7.parsers.Node;
import week7.parsers.Parser;

public class LL1 extends Parser {

    public LL1(String input) {
        super(input);
    }

    public static void main(String[] args) {
        Parser parser = new LL1(args[0]);
        parser.testRecognizer();
    }

    // Grammatika reeglid:
    // S -> aSb | ε
    protected Node s() {
        switch (peek()) {
            case 'a':
                // S -> aSb
                match('a');
                s();
                match('b');
                break;
            case 'b':
            case '$':
                // S -> ε
                epsilon();
                break;
            default:
                expected('a', 'b', '$');
        }
        return null;
    }
}
