package week5.javaAst.example;

import org.eclipse.jdt.core.dom.*;
import week5.javaAst.JavaASTUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

// https://courses.cs.ut.ee/2019/AKT/spring/Main/JavaAST
public class SimpleJavaASTDemo {
	public static void main(String[] args) throws IOException {
        Path defaultFile = Paths.get("src", "main", "java", "week5", "javaAst", "example", "ASTViewDemo.java");
        Path inputFile = args.length > 0 ? Paths.get(args[0]) : defaultFile;

        // eeldan, et fail on UTF-8 kodeeringus
        String source = new String(Files.readAllBytes(inputFile), "UTF-8");
		
		ASTNode juurtipp = JavaASTUtils.parseCompilationUnit(source);

		Set<String> names = new HashSet<>();
		collectNames(juurtipp, names);
		
		for (String name : names) {
			System.out.println(name);
		}
	}

    private static void collectNames(ASTNode node, Set<String> names) {
		if (node instanceof SimpleName) {
			names.add(((SimpleName) node).getIdentifier());
		}
		
		for (ASTNode child : JavaASTUtils.getChildNodes(node)) {
			collectNames(child, names);
		}
	}
}
