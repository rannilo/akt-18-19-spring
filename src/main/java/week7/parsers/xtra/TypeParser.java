package week7.parsers.xtra;

import week7.parsers.Node;
import week7.parsers.Parser;
import week7.parsers.xtra.typeast.Type;

// S → T D
// T → 'int'
// T → 'void'
// D → '*' D
// D → A
// A → '(' D ')'
// A → A '[]'
// A → ε
public class TypeParser extends Parser {

    public TypeParser(String input) {
        super(input);
    }

    protected Node s() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Parser p = new TypeParser("int*[]");
        System.out.println(((Type) p.parse()).toEnglish());
    }
}
