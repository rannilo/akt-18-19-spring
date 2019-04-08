package week7.parsers.mini.levels.boring;

import week7.parsers.Node;
import week7.parsers.Parser;

public class MiniParser6 extends Parser {

    public MiniParser6(String input) {
        super(input);
    }

    public static Node parse(String src) {
        MiniParser6 parser = new MiniParser6(src);
        return parser.parse();
    }

    // S → aAa | ε
    protected Node s() {
        Node n = new Node('S');
        switch (peek()) {
            case 'a':
                n.add(match('a'));
                n.add(a());
                n.add(match('a'));
                break;
            default:
                n.add(epsilon());
        }
        return n;
    }

    // A → bSc | bB
    // A → b (Sc|B)
    private Node a() {
        Node n = new Node('A');
        n.add(match('b'));
        switch (peek()) {
            case 'b':
                n.add(b());
                break;
            default:
                n.add(s());
                n.add(match('c'));
        }
        return n;
    }

    // B → b | Bc
    // B → bc*
    private Node b() {
        Node n = new Node('B');
        n.add(match('b'));
        // Kui vasakrekursiooni Süntakspuu tundub keeruline, siis teeme ainult keele äratundmist:
        while (peek() == 'c') {
            match('c');
        }
        return n;
    }

}

