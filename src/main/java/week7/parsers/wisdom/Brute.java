package week7.parsers.wisdom;

import week7.parsers.Node;
import week7.parsers.ParseException;
import week7.parsers.Parser;

public class Brute extends Parser {
    public Brute(String input) {
        super(input);
    }

    // Grammatika reeglid:
    // S -> aSb | ε
    protected Node s() {
        int mark = pos;
        try {
            match('a');
            s();
            match('b');
        } catch (ParseException e) {
            pos = mark;
        }
        epsilon();
        return null;
    }
}