package week5.javaAst;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.fail;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProgramAnalysisTests {
	public static String lastTestDescription = "";

	@Test
	public void oneLetterNames1() {
		testOneLetterNames("public class Demo1 {\r\n" + 
				"	public static int f (int x, int y) {\r\n" + 
				"		int z = x + y;\r\n" + 
				"		return z;\r\n" + 
				"	}\r\n /* xx ss z */" + 
				"}", true);
		
		testOneLetterNames("public class Demo1 {\r\n" + 
				"	public static int fun (int xx, int yx) {\r\n" + 
				"		int zr = xx + yx;\r\n" + 
				"		String ss = \"a b c d\";\r\n" + 
				"		return zr; // x y z\r\n" + 
				"	}/* x y z xx yy zz zr */\r\n" + 
				"}", false);
	}

	@Test
	public void oneLetterNames2() {
		testOneLetterNames("public class NestedLoops1 {\r\n" + 
				"	void f() {\r\n" + 
				"		while (true) {\r\n" + 
				"			int[] a = new int[10];\r\n" + 
				"			\r\n" + 
				"			for (int x : a) {\r\n" + 
				"				\r\n" + 
				"				for (int j=0; j < 10; j++) {\r\n" + 
				"					\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		}\r\n" + 
				"	}/* x y z xx yy zz zr */\r\n" + 
				"}", true);
		
		testOneLetterNames("public class Demo1 {\r\n" + 
				"	public static int fun (int xx, int yx) {\r\n" + 
				"		int zr = xx + yx;\r\n" + 
				"		String ss = \"a b c d\";\r\n" + 
				"		return zr;\r\n" + 
				"	}\r\n /* x y z xx yy zz zr */" + 
				"}", false);
	}

	@Test
	public void oneLetterNames3() {
		testOneLetterNames("public class Demo1 {\r\n" + 
				"	public static int frr (int xxx, int y) {\r\n" + 
				"		int zww = xxx + y;\r\n" + 
				"		return zww;\r\n" + 
				"	}\r\n" + 
				"}", true);
		
		testOneLetterNames("public class CLaSS { }", false);
	}
	

	@Test
	public void oneLetterNames4() {
		testOneLetterNames("public class Demo1 {\r\n" + 
				"	public static int f (int xqq, int yqq) {\r\n" + 
				"		int zqq = xqq + yqq;\r\n" + 
				"		return zqq;\r\n" + 
				"	}/* x y z xx yy zz zr */\r\n" + 
				"}", true);
		
		testOneLetterNames("public class CLaSS { }/* x y z xx yy zz zr */", false);
	}

	@Test
	public void oneLetterNames5() {
		testOneLetterNames("public class C { }/* x y z xx yy zz zr */", true);
		
		testOneLetterNames("public class Demo1 {\r\n" + 
				"	public static int fun (int xx, int yx) {\r\n" + 
				"		int zr = xx + yx;\r\n" + 
				"		String ss = \"a b c d\";\r\n" + 
				"		return zr;\r\n" + 
				"	}/* x y z xx yy zz zr */\r\n" + 
				"}", false);

		testOneLetterNames("public class Demo1 {\r\n" + 
				"	public static int ff (int xx, int yy) {\r\n" + 
				"		int zz = xx + yy;\r\n" + 
				"		return zz;\r\n" + 
				"	}\r\n" + 
				"	public static int ff (int xx, int yy) {\r\n" + 
				"		int z = xx + yy;\r\n" + 
				"		return z;\r\n" + 
				"	}/* x y z xx yy zz zr */\r\n" + 
				"}", true);
		
		testOneLetterNames("public class Demo1 {\r\n" + 
				"	public static int fun (int xx, int yx) {\r\n" + 
				"		String ss = \"a b cc c d\";\r\n" + 
				"		return 3;\r\n" + 
				"	}/* x y z xx yy zz zr */\r\n" + 
				"}", false);
	}
	
	@Test
	public void testElses1() {
		testElses("public class C { }/* else if else */", 0);
		testElses("public class C { void f() {} }/* else if else */", 0);
		testElses("public class abc {\r\n" + 
				"	void f() {\r\n" + 
				"		if (true) {\r\n" + 
				"			System.out.println(\"jah\");\r\n" + 
				"		} else if (false || true) {\r\n" + 
				"			System.out.println(\"ei\");\r\n" + 
				"		}\r\n" + 
				"	}/* else if else */\r\n" + 
				"}\r\n" + 
				"", 1);
	}
	
	@Test
	public void testElses2() {
		testElses("public class C { }/* else if else */", 0);
		testElses("public class C { void f() {} }/* else if else */", 0);
		testElses("public class abc {\r\n" + 
				"	void f() {\r\n" + 
				"		if (true) {\r\n" + 
				"			System.out.println(\"if (kala) {} else { else }\");\r\n" + 
				"		} else {\r\n" + 
				"			System.out.println();\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"}/* else if else */\r\n" + 
				"", 1);
		
		testElses("public class abc {\r\n" + 
				"	void f() {\r\n" + 
				"		if (true) {\r\n" + 
				"			System.out.println(\"if (kala) {} else { else }\");\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"}/* else if else */\r\n" + 
				"", 0);
	}
	
	@Test
	public void testNestedLoops1() {
		testNestedLoops("public class SingleLoop {\r\n" + 
				"	void f() {\r\n" + 
				"		while (true) {\r\n" + 
				"			/* while */\r\n" + 
				"		    String ss = \" while for while \";\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"}", 0);
		
		testNestedLoops("\r\n" + 
				"public class NestedLoops1 {\r\n" + 
				"	void f() {\r\n" + 
				"		while (true) {\r\n" + 
				"			int[] a = new int[10];\r\n" + 
				"			\r\n" + 
				"			for (int x : a) {\r\n" + 
				"				\r\n" + 
				"				for (int j=0; j < 10; j++) {\r\n" + 
				"					/* while */\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"}\r\n" + 
				"", 2);
	}
	
	
	@Test
	public void testNestedLoops2() {
		testNestedLoops("public class SingleLoop {\r\n" + 
				"	void f() {\r\n" + 
				"		while (true) {\r\n" + 
				"			\r\n" + 
				"		}\r\n" + 
				"		while (true) {\r\n" + 
				"			/* while */\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"}", 0);
		
		testNestedLoops("\r\n" + 
				"public class NestedLoops1 {\r\n" + 
				"	void f(boolean cond) {\r\n" + 
				"		if (cond) {\r\n" + 
				"			while (cond) {\r\n" + 
				"				int[] a = new int[10];\r\n" + 
				"				\r\n" + 
				"				for (int x : a) {\r\n" + 
				"					\r\n" + 
				"					for (int j=0; j < 10; j++) {\r\n" + 
				"						\r\n" + 
				"					}\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"			\r\n" + 
				"			while (true) {\r\n" + 
				"				int[] a = new int[10];\r\n" + 
				"				/* while */\r\n" + 
				"				for (int x : a) {\r\n" + 
				"					\r\n" + 
				"					for (int j=0; j < 10; j++) {\r\n" + 
				"						\r\n" + 
				"					}\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"", 4);
	}
	
	
	@Test
	public void testUnusedVars1() {
		testUnusedVars("public class C { }", 0);
		
		testUnusedVars("public class abc {\r\n" + 
				"	void def() {\r\n" + 
				"		int x = 3;\r\n" + 
				"		String y;\r\n" + 
				"		String ss = \"x y z\";\r\n" + 
				"	}\r\n" + 
				"}\r\n" + 
				"", 3);
		
		testUnusedVars("public class abc {\r\n" + 
				"	void def() {\r\n" + 
				"		int x = 3;\r\n" + 
				"		String y = \"---\";\r\n" + 
				"		System.out.println(x + \" ja \" + y);\r\n" + 
				"	}\r\n" + 
				"}\r\n" + 
				"", 0);
		
		testUnusedVars("public class abc {\r\n" + 
				"	void def() {\r\n" + 
				"		int x = 3;\r\n" + 
				"		String y;\r\n" + 
				"		String ss = y + \"x y z\";\r\n" + 
				"	}\r\n" + 
				"}\r\n" + 
				"", 2);
		
	}


	private static void testOneLetterNames(String src, boolean expectedResult) {
		lastTestDescription = "Annan argumendiks sellise programmi:\n\n>"
                + src.replaceAll("\r\n", "\n>");
		
		if (SimpleProgramAnalysis.hasOneLetterNames(src) != expectedResult) {
			fail("Ootasin vastuseks " + expectedResult + ", aga sain " + !expectedResult);
		}
	}

	private static void testNestedLoops(String src, int expectedResult) {
		lastTestDescription = "Annan argumendiks sellise programmi:\n\n>"
                + src.replaceAll("\r\n", "\n>");
		
		int actualResult = SimpleProgramAnalysis.numberOfNestedLoops(src);
		if (actualResult != expectedResult) {
			fail("Ootasin vastuseks " + expectedResult + ", aga sain " + actualResult);
		}
	}
	
	private static void testUnusedVars(String src, int expectedResult) {
		lastTestDescription = "Annan argumendiks sellise programmi:\n\n>"
                + src.replaceAll("\r\n", "\n>");
		
		int actualResult = SimpleProgramAnalysis.numberOfUnusedVariables(src);
		if (actualResult != expectedResult) {
			fail("Ootasin vastuseks " + expectedResult + ", aga sain " + actualResult);
		}
	}
	
	private static void testElses(String src, int expectedResult) {
		lastTestDescription = "Annan argumendiks sellise programmi:\n\n>"
                + src.replaceAll("\r\n", "\n>");
		
		int actualResult = SimpleProgramAnalysis.numberOfElseKeywords(src);
		if (actualResult != expectedResult) {
			fail("Ootasin vastuseks " + expectedResult + ", aga sain " + actualResult);
		}
	}
	
	
}
