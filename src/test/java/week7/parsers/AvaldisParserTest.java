package week7.parsers;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week7.parsers.astdemo.AvaldisAst;
import week7.parsers.astdemo.AvaldisParser;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AvaldisParserTest {

    TestParser<AvaldisParser> tutParser = new TestParser<>(AvaldisParser.class);
    TestParser<AvaldisAst> tutAst = new TestParser<>(AvaldisAst.class);

    @Test
    public void test01_Recognizer() throws Exception {
        tutParser.accepts("x*x+x");
        tutParser.rejects("x+)");
    }

    @Test
    public void test02_Parse() throws Exception {
        tutParser.accepts("x*x", "S(T(F(x),Q(*,F(x),Q(ε))),R(ε))");
    }

    @Test
    public void test03_Expected() throws Exception {
        tutParser.rejects("x+-x", 2, "(x", "+*$)");
    }

    @Test
    public void test04_Ast1() throws Exception {
        tutAst.accepts("x*x", "*(x,x)");
    }

    @Test
    public void test05_Ast2() throws Exception {
        tutAst.accepts("x+x*x", "+(x,*(x,x))");
        tutAst.accepts("(x+x)*x", "*(+(x,x),x)");
        tutAst.accepts("x*x+x", "+(*(x,x),x)");
        tutAst.accepts("x*(x+x)", "*(x,+(x,x))");
    }

    @Test
    public void test06_Ast3() throws Exception {
        tutAst.accepts("x+x+x", "+(+(x,x),x)");
        tutAst.accepts("x*x*x", "*(*(x,x),x)");
    }

}