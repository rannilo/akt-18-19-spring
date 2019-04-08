package week7.parsers.mini.levels.boring;

import week7.parsers.Node;
import week7.parsers.Parser;

public class MiniParser4 extends Parser {

    public MiniParser4(String input) {
        super(input);
    }

    public static Node parse(String src) {
        MiniParser4 parser = new MiniParser4(src);
        return parser.parse();
    }

    // S → aAa | ε
    protected Node s() {
        switch (peek()) {
            case 'a':
                match('a');
                a();
                match('a');
                break;
            default:
                epsilon();
        }
        return null;
    }

    // A → bSc | bB
    // A → b (Sc|B)
    private Node a() {
        match('b');
        switch (peek()) {
            case 'b':
                b();
                break;
            default:
                s();
                match('c');
        }
        return null;
    }

    // B → b
    private Node b() {
        match('b');
        return null;
    }

}

