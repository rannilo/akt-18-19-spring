package week5.javaAst.example;

// https://courses.cs.ut.ee/2019/AKT/spring/Main/JavaAST
public class ASTViewDemo {
	public static void main(String[] args) {
		System.out.println(arvuta(3, 6));
	}
	
	private static int arvuta(int a, int b) {
		return a + b * 2;
	}
}
