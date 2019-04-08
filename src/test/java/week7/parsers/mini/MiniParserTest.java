package week7.parsers.mini;

import org.junit.Test;
import week7.parsers.TestParser;
import week7.parsers.mini.levels.awesome.MiniParser;

public class MiniParserTest {

    private TestParser<MiniParser> parser = new TestParser<>(MiniParser.class);

    // Tase 1
    // S → aAa | ε
    // A → bB
    // B → b

    @Test
    public void testParse01() throws Exception {
        parser.accepts("abba");
        parser.rejects("a");
        parser.rejects("b");
    }

    @Test
    public void testParse02() throws Exception {
        parser.accepts("");
        parser.accepts("abba");
        parser.rejects("aa");
        parser.rejects("baba");
    }

    // Tase 2
    // S → aAa | ε
    // A → bB | bSc
    // B → b

    @Test
    public void testParse03() throws Exception {
        parser.accepts("");
        parser.accepts("abba");
        parser.accepts("abca");
        parser.rejects("acba");
        parser.rejects("a");
    }

    @Test
    public void testParse04() throws Exception {
        parser.accepts("");
        parser.accepts("abba");
        parser.accepts("abca");
        parser.accepts("ababbaca");
        parser.accepts("ababcaca");
        parser.accepts("abababbacaca");
        parser.accepts("abababcacaca");
        parser.accepts("ababababbacacaca");
        parser.rejects("ababbbaca");
        parser.rejects("ababcaa");
        parser.rejects("abababbacac");
        parser.rejects("bababcacaca");
        parser.rejects("ababababacacaca");
    }


    @Test
    public void testParse05() throws Exception {
        parser.accepts("", "S(ε)");
        parser.accepts("abba", "S(a,A(b,B(b)),a)");
        parser.accepts("abababcacaca", "S(a,A(b,S(a,A(b,S(a,A(b,S(ε),c),a),c),a),c),a)");
        parser.accepts("abababbacaca", "S(a,A(b,S(a,A(b,S(a,A(b,B(b)),a),c),a),c),a)");
    }

    // Tase 3
    // S → aAa | ε
    // A → bB | bSc
    // B → b | Bc

    @Test
    public void testParse06() throws Exception {
        parser.accepts("abbca");
        parser.accepts("abbcccca");
        parser.accepts("abababbccccccacaca");
        parser.rejects("abcaa");
        parser.rejects("abbcccc");
        parser.rejects("abababbbccccccacaca");
    }

    @Test
    public void testParse07() throws Exception {
        parser.accepts("abbca", "S(a,A(b,B(B(b),c)),a)");
        parser.accepts("abababbccccccacaca", "S(a,A(b,S(a,A(b,S(a,A(b,B(B(B(B(B(B(B(b),c),c),c),c),c),c)),a),c),a),c),a)");
    }

    @Test
    public void testParse08() throws Exception {
        parser.rejects("aa", 1, "b");
        parser.rejects("abxa", 2, "abc");
        parser.rejects("abbccccxac", 7, "ac");
    }
    

}