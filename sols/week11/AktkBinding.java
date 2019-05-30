package week11;

import week9.ast.*;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class AktkBinding {
	private static final String RETURN_PSEUDO_BINDING = "__return__";

	/**
	 * Määra iga antud programmis olevale muutujaviitele (week9.ast.Variable)
	 * teda siduv element (week9.ast.VariableBinding,
	 * st week9.ast.VariableDeclaration või week9.ast.FunctionParameter)
	 * Kasuta selleks meetodit week9.ast.Variable#setBinding.
	 * 
	 * Kui muutuja kasutusele ei vasta ühtegi deklaratsiooni ega parameetrit, 
	 * siis jäta binding määramata.
	 */
	public static void bind(AstNode node) {
		new AktkBindingVisitor().visit(node);
	}

	private static class AktkBindingVisitor extends AstVisitor.VoidVisitor {

		private final BindingEnvironment env = new BindingEnvironment();

		@Override
		protected void visitVoid(Assignment assignment) {
			// Seda ei olnud eelmistes kodutöödes, aga kui tahta seda järgmiste kodutööde jaoks kasutada,
			// siis on ka omistamise juures vaja kätte saada vastav binding.
			assignment.setBinding((VariableBinding) env.get(assignment.getVariableName()));
			visit(assignment.getExpression());
		}

		@Override
		protected void visitVoid(Block block) {
			env.enterBlock();
			for (Statement statement : block.getStatements()) {
				visit(statement);
			}
			env.exitBlock();
		}

		@Override
		protected void visitVoid(ExpressionStatement expressionStatement) {
			visit(expressionStatement.getExpression());
		}

		@Override
		protected void visitVoid(FunctionCall functionCall) {
			functionCall.setFunctionBinding((FunctionDefinition) env.get(functionCall.getFunctionName()));

			for (Expression expression : functionCall.getArguments()) {
				visit(expression);
			}
		}

		@Override
		protected void visitVoid(FunctionDefinition functionDefinition) {
			env.declare(functionDefinition.getName(), functionDefinition);
			env.enterBlock();
			for (FunctionParameter param : functionDefinition.getParameters()) {
				env.declare(param.getVariableName(), param);
			}
			env.declare(RETURN_PSEUDO_BINDING, functionDefinition);
			visit(functionDefinition.getBody());
			env.exitBlock();
		}

		@Override
		protected void visitVoid(IfStatement ifStatement) {
			visit(ifStatement.getCondition());
			visit(ifStatement.getThenBranch());
			visit(ifStatement.getElseBranch());
		}

		@Override
		protected void visitVoid(IntegerLiteral integerLiteral) {

		}

		@Override
		protected void visitVoid(ReturnStatement returnStatement) {
			returnStatement.setFunctionBinding((FunctionDefinition) env.get(RETURN_PSEUDO_BINDING));
			visit(returnStatement.getExpression());
		}

		@Override
		protected void visitVoid(StringLiteral stringLiteral) {

		}

		@Override
		protected void visitVoid(Variable variable) {
			variable.setBinding((VariableBinding) env.get(variable.getName()));
		}

		@Override
		protected void visitVoid(VariableDeclaration variableDeclaration) {
			if (variableDeclaration.getInitializer() != null)
				visit(variableDeclaration.getInitializer());
			env.declare(variableDeclaration.getVariableName(), variableDeclaration);
		}

		@Override
		protected void visitVoid(WhileStatement whileStatement) {
			visit(whileStatement.getCondition());
			visit(whileStatement.getBody());
		}
	}

	private static class BindingEnvironment {
		private Deque<Map<String, AstNode>> namespaces = new LinkedList<>();

		public void enterBlock() {
			namespaces.push(new HashMap<>());
		}

		public void exitBlock() {
			namespaces.pop();
		}

		public AstNode get(String name) {
			Map<String, AstNode> namespace = find(name);
			return namespace != null ? namespace.get(name) : null;
		}

		public void declare(String name, AstNode value) {
			namespaces.peek().put(name, value);
		}

		private Map<String, AstNode> find(String name) {
			for (Map<String, AstNode> block : namespaces) {
				if (block.containsKey(name)) {
					return block;
				}
			}

			return null;
		}
	}

}
