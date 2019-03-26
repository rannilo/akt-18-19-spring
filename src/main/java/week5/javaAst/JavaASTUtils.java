package week5.javaAst;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class JavaASTUtils {

	public static CompilationUnit parseCompilationUnit(String source) {
		ASTParser parser = ASTParser.newParser(AST.JLS11);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		return (CompilationUnit) parser.createAST(null);
	}

	public static List<ASTNode> getChildNodes(ASTNode node) {

		final List<ASTNode> result = new ArrayList<>();

		for (Object childDesc : node.structuralPropertiesForType()) {
			Object child = node.getStructuralProperty((StructuralPropertyDescriptor) childDesc);
			if (child instanceof ASTNode) {
				result.add((ASTNode) child);
			}
			else if (child instanceof List) {
				for (Object item : (List<?>)child) {
					if (item instanceof ASTNode) {
						result.add((ASTNode) item);
					}
				}
			}
			else {
				// String or int or smth like that. Skip these
			}
		}

		return result;
	}
}
