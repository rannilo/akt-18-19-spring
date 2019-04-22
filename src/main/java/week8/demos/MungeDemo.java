package week8.demos;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class MungeDemo {

    public static String parse(String input) {
        MungeLexer lexer = new MungeLexer(CharStreams.fromString(input));
        MungeParser parser = new MungeParser(new CommonTokenStream(lexer));
        return parser.init().toStringTree(parser);
    }

    // L1: 'a' 'ba'*;
    // L2: 'b' 'ab'*;
    // L3: 'abd';
    // L4: 'd'+;

    public static void main(String[] args) {
        System.out.println(parse("babadd"));
        System.out.println(parse("ababdddd"));
        System.out.println(parse("dabbab"));
        System.out.println(parse("abddb"));
        System.out.println(parse("babdda"));
    }
}
