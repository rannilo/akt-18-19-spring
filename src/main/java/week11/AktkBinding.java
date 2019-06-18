package week11;

import week9.ast.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;


public class AktkBinding {

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
		AktkEnvironment env = new AktkEnvironment();
		AstVisitor.VoidVisitor visitor = new AstVisitor.VoidVisitor() {
			@Override
			protected void visitVoid(Assignment assignment) {
				assignment.setBinding((VariableBinding)env.get(assignment.getVariableName()));
			}

			@Override
			protected void visitVoid(Block block) {
				env.enterBlock();
				for (Statement s : block.getStatements()){
					this.visit(s);
				}
				env.exitBlock();
			}

			@Override
			protected void visitVoid(ExpressionStatement expressionStatement) {
				this.visit(expressionStatement.getExpression());
			}

			@Override
			protected void visitVoid(FunctionCall functionCall) {
				if(env.get(functionCall.getFunctionName()) != null) {
					functionCall.setFunctionBinding((FunctionDefinition) env.get(functionCall.getFunctionName()));
				}
				env.enterBlock();
				for (Expression e : functionCall.getArguments()){
					this.visit(e);
				}
				env.exitBlock();
			}

			@Override
			protected void visitVoid(FunctionDefinition functionDefinition) {
				AktkEnvironment.currentFunction = functionDefinition;
				env.declare(functionDefinition.getName());
				env.assign(functionDefinition.getName(), functionDefinition);
				env.enterBlock();
				for(FunctionParameter fp : functionDefinition.getParameters()){
					env.declare(fp.getVariableName());
					env.assign(fp.getVariableName(), fp);
				}
				for(Statement s: functionDefinition.getBody().getStatements()){
					visit(s);
				}
				env.exitBlock();
				AktkEnvironment.currentFunction = null;
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
				returnStatement.setFunctionBinding(AktkEnvironment.currentFunction);
				this.visit(returnStatement.getExpression());
			}

			@Override
			protected void visitVoid(StringLiteral stringLiteral) {

			}

			@Override
			protected void visitVoid(Variable variable) {
				variable.setBinding((VariableBinding)env.get(variable.getName()));
			}

			@Override
			protected void visitVoid(VariableDeclaration variableDeclaration) {
				env.declare(variableDeclaration.getVariableName());
				env.assign(variableDeclaration.getVariableName(), variableDeclaration);
				if(variableDeclaration.getInitializer()!= null) visit(variableDeclaration.getInitializer());
			}

			@Override
			protected void visitVoid(WhileStatement whileStatement) {
				whileStatement.getCondition();
				visit(whileStatement.getBody());
			}
		};
		visitor.visit(node);
	}
}


class AktkEnvironment {

	public static FunctionDefinition currentFunction;
	Stack<Map<String, Object>> data;

	public AktkEnvironment(){
		data = new Stack<>();
		data.add(new HashMap<>());
	}

	public void declare(String variable) {
		data.peek().put(variable, null);
	}

	public void assign(String variable, Object value) {
		for (int i = data.size()-1; i>= 0; i--){
			if(data.get(i).containsKey(variable)){
				data.get(i).put(variable, value);
				return;
			}
		}
		throw new RuntimeException("Muutujat pole deklareeritud");
	}

	public Object get(String variable) {
		for (int i = data.size()-1; i>= 0; i--){
			Object value = data.get(i).getOrDefault(variable, null);
			if(value != null) return value;
		}
		return null;
	}

	public void enterBlock() {
		data.push(new HashMap<>());
	}

	public void exitBlock() {
		data.pop();
	}

	public FunctionDefinition getLastFunctionDefinition() {
		for (int i = data.size()-1; i>= 0; i--){
			Map<String, Object> value = data.get(i);
			for(String s: value.keySet()){
				if(value.get(s) instanceof FunctionDefinition){
					return (FunctionDefinition)value.get(s);
				}
			}
		}
		return null;
	}
}

