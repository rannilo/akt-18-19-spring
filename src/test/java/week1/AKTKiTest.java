package week1;

import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AKTKiTest {
    @ClassRule public static Timeout classTimeout = new Timeout(30, TimeUnit.SECONDS);
    @Rule public Timeout timeout = new Timeout(5, TimeUnit.SECONDS);

    public static String lastTestDescription = "";

    public static final String DEFAULT_FILENAME = "test_program_for_junit.aktk";

    @Test
    public void test01_basicPrint() throws IOException {
        check("print 3", "3");
        check("print 7", "7");
    }

    @Test
    public void test02_basicPrintAbsoluteFilename() throws IOException {
        check("print 3", "3");
        check("print 8753", "8753");
    }

    @Test
    public void test03_singlePlusOrSingleMinus() throws IOException {
        check("print 3 + 2", "5");
        check("print 14+0", "14");
        check("print 103 + 5", "108");
        check("print 103+5", "108");
        check("print 103 +  5", "108");
        check("print 30 - 2", "28");
        check("print 3 - 15", "-12");
        check("print 3-15", "-12");
        check("print             3-           15   ", "-12");
    }

    @Test
    public void test04_severalPrints() throws IOException {
        check("print 3\n"
                + "print 4\n", "3\n4");
    }

    @Test
    public void test05_plusAndMinus() throws IOException {
        check("print 3 + 2 - 40", "-35");
        check("print 1+2+3+4+5+6+7+8+9+10+11-11-10-9-8-7-6-5-4", "6");
    }

    @Test
    public void test06_simpleVariables() throws IOException {
        check(    "x=34\n"
                + "print x", "34");

        check(    "p=34\n"
                + "print p", "34");

        check(    "x=34\n"
                + "p=4\n"
                + "print p\n"
                + "print x", "4\n34");
    }

    @Test
    public void test07_variablesAndExpressions() throws IOException {
        check(    "x=34\n"
                + "p=x-x+1\n"
                + "print p", "1");

        check(    "x=34\n"
                + "p=x-x+1\n"
                + "print p + p + x", "36");
    }

    @Test
    public void test08_commentsAndEmptyLines() throws IOException {
        check(    "x=34\n"
                + "p=x-x+1\n"
                + "\n"
                + "# kommentaar\n"
                + "\n"
                + "# kommentaar\n"
                + "print p", "1");

        check(    "x=34\n"
                + "p=x-x+1 # kommentaar\n"
                + "print p + p + x # kommentaar", "36");
    }

    @Test
    public void test09_undefinedVariables() throws IOException {
        checkError("print x");
        checkError("print w");
        checkError("print x\n"
                +  "x = 3");
    }

    @Test
    public void test10_syntaxErrors() throws IOException {
        checkError("kala=66");
        checkError("x=6.6");
        checkError("x=");
        checkError("x=3+");
        checkError("x=3-");
        checkError("print");
        checkError("print 3+");
        checkError("print 3-");
        checkError("prnt 3");
    }

    private void checkError(String program) throws IOException {
        lastTestDescription = "Programm: \n>" + quoteTextBlock(program);
        File f = createFile(DEFAULT_FILENAME, program);
        try {
            ExecutionResult result = runJavaProgramViaClassReload("week1.AKTKi", DEFAULT_FILENAME);
            // stderr voos peaks olema veateade
            if (result.err.isEmpty()) {
                fail("Ootasin veateadet, aga seda polnud. Väljund oli " + result.out);
            }
        }
        finally {
            f.delete();
        }
        lastTestDescription = "";
    }

    private void check(String program, String expectedOutput) throws IOException {
        check(program, expectedOutput, DEFAULT_FILENAME);
    }

    private void check(String program, String expectedOutput, String filename) throws IOException {
        lastTestDescription = "Programm: \n>" + quoteTextBlock(program);

        File f = createFile(filename, program);
        try {
            ExecutionResult result = runJavaProgramViaClassReload("week1.AKTKi", filename);

            // Väljundi kontrollimisel pean arvestama, et Windowsi reavahetus on \r\n, aga mujal on \n.
            // Samuti tahan ma olla paindlik selle suhtes, kas väljundi lõpus on reavahetus või mitte.
            expectedOutput = expectedOutput.replace("\r\n", "\n").replaceFirst("\\n$", "");
            String actualOutput = result.out.replace("\r\n", "\n").replaceFirst("\\n$", "");

            if (!expectedOutput.equals(actualOutput)) {
                fail("Ootasin väljundit\n" + quoteTextBlock(expectedOutput)
                    + ", aga väljund oli\n" + quoteTextBlock(actualOutput));
            }
            assertEquals(expectedOutput, actualOutput);

            // stderr voog peaks olema tühi, st. programm ei tohiks anda vigu
            assertTrue(result.err.isEmpty());
        }
        finally {
            f.delete();
        }
        lastTestDescription = "";
    }

    private String quoteTextBlock(String s) {
        return "\n>" + s.replace("\n", "\n>") + "\n";
    }

    static File createFile(String name, String content) throws IOException {
        File f = new File(name);
        PrintWriter fw = new PrintWriter(f, "UTF-8");
        fw.write(content);
        fw.close();
        return f;
    }

    public static ExecutionResult runJavaProgramViaClassReload(String className, String... args) throws IOException {
        return runJavaProgramWithInput(className, "", args);
    }

    public static ExecutionResult runJavaProgramWithInput(String className, String input, String... args) throws IOException {
        InputStream origIn = System.in;
        PrintStream origOut = System.out;
        PrintStream origErr = System.err;

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // avoid changing all throws signatures although ideally should
        }

        // https://stackoverflow.com/a/20094513/854540
        URL[] urls = {clazz.getProtectionDomain().getCodeSource().getLocation()};
        try (URLClassLoader classLoader = new URLClassLoader(urls, clazz.getClassLoader().getParent())){

            // https://stackoverflow.com/a/1119559/854540
            System.setIn(in);
            System.setOut(new PrintStream(out));
            System.setErr(new PrintStream(err));

            Class<?> reloadedClazz = classLoader.loadClass(clazz.getName());

            Method main = reloadedClazz.getMethod("main", String[].class);
            main.invoke(null, new Object[]{args});

            return new ExecutionResult(out.toString("UTF-8"), err.toString("UTF-8"));
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e); // avoid changing all throws signatures although ideally should
        }
        catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace(); // ugh
            return new ExecutionResult(out.toString("UTF-8"), err.toString("UTF-8"));
        }
        finally {
            System.setIn(origIn);
            System.setOut(origOut);
            System.setErr(origErr);
        }
    }

    public static class ExecutionResult {
        public ExecutionResult(String out, String err) {
            this.out = out;
            this.err = err;
        }
        public String out;
        public String err;

        @Override
        public String toString() {
            return String.format("ExecutionResult{out='%s', err='%s'}", out, err);
        }
    }

}
