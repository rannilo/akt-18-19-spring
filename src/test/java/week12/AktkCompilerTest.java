package week12;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;
import week11.AktkBinding;
import week9.AktkAst;
import week9.ast.AstNode;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkCompilerTest {
	public static String lastTestDescription = "";
	public static String successMessage = "kompileeritud programm töötas õigesti!";

	private static final Path AKTK_OUT = Paths.get("aktk-out");
	
	@Rule
	public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

	@Test
	public void test01_expressions() {
		//testProgram("print(1+1)", "test01", "", "2");
		testProgram("1", "test01", "", "");
		testProgram("print(-1)", "test01", "", "-1");
	}

	@Test
	public void test02_variables() {
		testProgram("var x = 1; print(x)", "test02_1", "", "1");
		testProgram("var x = 1", "test02", "", "");
	}

	@Test
	public void test03_output() {
		testProgram("print(42)", "test03", "", "42\n");
	}

	@Test
	public void test04_input() {
		testProgram("var x = readInt(); print(x)", "test04", "127", "127\n");
	}

	@Test
	public void test05_successor() {
		Map<String, String> ioPairs = new HashMap<String, String>();
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < 5; i++) {
			int x = r.nextInt(888);
			ioPairs.put(String.valueOf(x), String.valueOf(x+1) + "\n");
		}

		testProgram("var x = readInt(); print(x+1)", "test05", ioPairs);
	}

	@Test
	public void test06_whileStatements() {
		testProgram("while 0 do {\n"
				+ "   print(n*n);"
				+ "   n = 0"
				+ "}\n" +
				"print(1)", "test06_1", "", "1");
		testProgram("var n = readInt();\n"
				+ "while n > 0 do {\n"
				+ "   print(n*n);"
				+ "   n = n - 1"
				+ "}\n", "test06", "4\n", "16\n9\n4\n1\n");
	}

	@Test
	public void test07_ifStatements() {
		testProgram("if 4 - 2 == 2 then {\n"
				+ "   print(100)\n"
				+ "} else print(1) \n", "test07_1", "", "100");

		Map<String, String> ioPairs = new HashMap<String, String>();
		ioPairs.put("24\n", "4\n");
		ioPairs.put("6\n", "2\n");
		ioPairs.put("7\n", "1\n");

		testProgram("var n = readInt();\n"
				+ "if n % 2 == 0 then {\n"
				+ "   if (n / 2) % 2 == 0 then print(4)\n"
				+ "   else print(2)"
				+ "} else print(1) \n", "test07", ioPairs);
	}

	@Test
	public void test08_builtins() {
		Map<String, String> ioPairs = new HashMap<String, String>();
		ioPairs.put("234\n12\n", "6\n6\n");
		ioPairs.put("67\n7\n", "1\n1\n");

		testProgram("var a = readInt();\r\n" +
				"var b = readInt();\r\n" +
				"\r\n" +
				"/* Arvutan suurima ühisteguri \"standardteegi\" abil */\r\n" +
				"print(gcd(a,b));\r\n" +
				"\r\n" +
				"\r\n" +
				"/* Nüüd arvutan sama asja aktk vahenditega */\r\n" +
				"var c;\r\n" +
				"while b > 0 do {\r\n" +
				"	c = a % b;\r\n" +
				"	a = b;\r\n" +
				"	b = c\r\n" +
				"};\r\n" +
				"\r\n" +
				"print(a)", "test08", ioPairs);
	}

	@Test
	public void test09_interactive() {
		Map<String, String> ioPairs = new HashMap<String, String>();
		ioPairs.put("234\n12\n0\n", "1\n1\n0\n");
		ioPairs.put("0\n", "0\n");
		ioPairs.put("-5\n0\n", "-1\n0\n");

		testProgram("/* Küsib standardsisendist täisarvu ja väljastab selle märgi.\r\n" +
				"   Kordab seda niikaua kuni sisestatakse 0 */\r\n" +
				"   \r\n" +
				"var x = readInt();\r\n" +
				"\r\n" +
				"while x != 0 do {\r\n" +
				"    if x > 0 then print(1) else print(-1);\r\n" +
				"    x = readInt()\r\n" +
				"};\r\n" +
				"\r\n" +
				"print(x)", "test09", ioPairs);
	}

	@Test
	public void test10_scopes() {
		Map<String, String> ioPairs = new HashMap<String, String>();
		ioPairs.put("4\n", "4\n4\n");
		ioPairs.put("5\n", "1\n5\n");

		testProgram("var x = readInt();\n" +
				"\n" +
				"if x % 2 == 0 then {\n" +
				"    print(x)\n" +
				"} else {\n" +
				"    var x = 1;\n" +
				"    print(x)\n" +
				"};\n" +
				"\n" +
				"print(x)", "test10", ioPairs);
	}

	private void testProgram(String aktkSource, String className,
							 String input, String expectedOutput) {
		Map<String, String> ioPairs = new HashMap<String, String>();
		ioPairs.put(input, expectedOutput);
		testProgram(aktkSource, className, ioPairs);
	}

	private void testProgram(String aktkSource, String className,
							 Map<String, String> ioPairs) {
		lastTestDescription = "Katsetan sellist programmi:\n\n>"
				+ aktkSource.replaceAll("\r\n", "\n").replaceAll("\n", "\n>");



		try {
			compileProgram(aktkSource, className);

			for (Map.Entry<String, String> ioPair : ioPairs.entrySet()) {
				String input = ioPair.getKey();
				String expectedOutput = ioPair.getValue();

				try {
					ExecutionResult result = runJavaClass(className, input);

					// Väljundi kontrollimisel pean arvestama, et Windowsi reavahetus on \r\n, aga mujal on \n.
					// Samuti tahan ma olla paindlik selle suhtes, kas väljundi lõpus on reavahetus või mitte.
					expectedOutput = expectedOutput.replace("\r\n", "\n").replaceFirst("\\n$", "");
					String actualOutput = result.out.replace("\r\n", "\n").replaceFirst("\\n$", "");

					if (!result.err.isEmpty()) {
						fail("Sisendiga\n>" + input.replaceAll("\n", "\n>") + "\nandis programm vea: "
								+ result.err);
					}
					/*else if (result.returnCode != 0) {
						fail("Sisendiga\n>" + input.replaceAll("\n", "\n>") + "\nandis programm veakoodi "
								+ result.returnCode);
					}*/
					else if (!actualOutput.equals(expectedOutput)) {
						fail("Sisendiga\n>" + input.replaceAll("\n", "\n>") + "\npidi tulema väljund\n>"
								+ expectedOutput.replaceAll("\n", "\n>") + "\naga tuli\n>"
								+ actualOutput.replaceAll("\n", "\n>"));
					}
				}
				catch (Exception e) {
					fail("Sisendiga\n" + input.replaceAll("\n", "\n>") + "\nsain vea:" + e);
				}
			}
			// Kui testid läbisid, siis kustutame ka klassi ära:
            //Files.deleteIfExists(AKTK_OUT.resolve(className + ".class"));
		} catch (Exception e) {
			fail("Kompileerimisel sain vea: " + e);
		}
	}

	private void compileProgram(String aktkSource, String className) throws IOException {
		AstNode ast = AktkAst.createAst(aktkSource);
		AktkBinding.bind(ast);
		byte[] bytes = AktkCompiler.createClass(ast, className);
		Files.createDirectories(AKTK_OUT);
		Files.write(AKTK_OUT.resolve(className + ".class"), bytes);
	}

	public static ExecutionResult runJavaClass(String className, String input, String... args) throws IOException {
        Class<?> reloadClazz = AktkCompilerBuiltins.class;

		InputStream origIn = System.in;
		PrintStream origOut = System.out;
		PrintStream origErr = System.err;

		ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

		// https://stackoverflow.com/a/20094513/854540
		URL[] urls = {AKTK_OUT.toUri().toURL(), reloadClazz.getProtectionDomain().getCodeSource().getLocation()};
		try (URLClassLoader classLoader = new URLClassLoader(urls, reloadClazz.getClassLoader().getParent())){

			// https://stackoverflow.com/a/1119559/854540
			System.setIn(in);
			System.setOut(new PrintStream(out));
			System.setErr(new PrintStream(err));

			Class<?> clazz = classLoader.loadClass(className);

			Method main = clazz.getMethod("main", String[].class);
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
