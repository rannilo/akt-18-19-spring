package week5.demos;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week5.expr.ExprNode;
import week5.regex.RegexNode;
import week5.regex.RegexParser;

import static org.junit.Assert.assertEquals;
import static week5.demos.PrettyPrinter.pretty;
import static week5.expr.ExprNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PrettyPrinterTest {

    private ExprNode expr1 = div(add(num(5), add(num(3), neg(num(2)))), num(2));
    private ExprNode expr2 = add(add(num(5), div(num(3), neg(num(2)))), num(2));
    private ExprNode expr3 = div(div(num(8), num(2)), num(2));
    private ExprNode expr4 = div(num(8), div(num(2), num(2)));

    @Test
    public void test01_TestPrettyprinter() {
        assertEquals("10", pretty(num(10)));
        assertEquals("2+2", pretty(add(num(2), num(2))));
        assertEquals("8/(2+2)", pretty(div(num(8), add(num(2), num(2)))));
        assertEquals("(5+3+-2)/2", pretty(expr1));
        assertEquals("5+3/-2+2", pretty(expr2));
        assertEquals("8/2/2", pretty(expr3));
        assertEquals("8/(2/2)", pretty(expr4));
    }

    @Test
    public void test02_PrettyRegex()  {
        checkRegex("a");
    }

    @Test
    public void test03_PrettyRegexBasics() {
        checkRegex("a");
        checkRegex("abc");
        checkRegex("a|ε");
        checkRegex("(ab)*");
    }

    @Test
    public void test04_PrettyRegexDiff()  {
        checkRegex("a");
        checkRegex("a*");
        checkRegex("abc");
        checkRegex("a|ε");
        checkRegex("(ab)*");
        checkRegex("(ab*)*");
        checkRegex("(b*)*");
        checkRegex("ab*cd*");
        checkRegex("(a|b)cd");
        checkRegex("a(b|c)d");
        checkRegex("ab(c|d)");
        checkRegex("a(b|c)d");
        checkRegex("a|b|c");
    }

    private void checkRegex(String input) {
        RegexNode node = RegexParser.parse(input);
        assertEquals(input, pretty(node));
    }
}