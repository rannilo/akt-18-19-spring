package week7.parsers;

import java.io.FileNotFoundException;

public abstract class Parser {
    private String input;
    protected int pos;

    public Parser(String input) {
        this.input = input + '$';
    }

    protected char peek() {
        return input.charAt(pos);
    }

    protected Node pop() {
        return new Node(input.charAt(pos++));
    }

    protected Node match(char c) {
        char y = input.charAt(pos);
        if (y == c) {
            pos++;
            return new Node(c);
        } else
            throw new ParseException(y, pos, c);
    }

    protected Node match(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) sb.append(match(c));
        return new Node(sb.toString());
    }

    protected Node epsilon() {
        return new Node('\u03B5');
    }

    private void done() {
        if (pos < input.length()-1) {
            char y = input.charAt(pos);
            throw new ParseException(y, pos, '$');
        }
    }

    protected void expected(Character... expected) {
        char y = input.charAt(pos);
        throw new ParseException(y, pos, expected);
    }

    public void testParser() {
        System.out.println("Parsing: " + input);
        try {
            Node root = parse();
            System.out.print("ACCEPT: ");
            System.out.println(root);
            root.makeDot();
        } catch (ParseException e) {
            System.out.println("Parse error: " + e.getMessage());
            System.out.println(input);
            for (int i = 0; i < e.getOffset(); i++)
                System.out.print(' ');
            System.out.println('^');
        } catch (FileNotFoundException e) {
            System.out.println("Failed to create dot file.");
        }
    }

    public void testRecognizer() {
        System.out.println("Parsing: " + input);
        try {
            parse();
            System.out.print("ACCEPT!");
        } catch (ParseException e) {
            System.out.println("REJECT!");
        }
    }

    public Node parse() {
        pos = 0;
        Node root = s();
        done();
        return root;
    }

    // Start symbol S.
    protected abstract Node s();

}
