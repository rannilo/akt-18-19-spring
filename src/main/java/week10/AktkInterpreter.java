package week10;
import week9.AktkAst;
import week9.ast.*;

import java.lang.reflect.Method;
import java.util.*;

public class AktkInterpreter {
	public static void main(String[] args){
		Map<String, Object> map = new HashMap<>();
		map.put("a", "aa");
		map.put("b", null);
		map.put("b", "a");
		System.out.println(map.containsKey("b"));
		System.out.println(map.get("b"));
	}

	public static void run(String program) {
		AstNode ast = AktkAst.createAst(program);
		AktkEnvironment env = new AktkEnvironment();
		AstVisitor<Object> visitor = new AstVisitor<>() {
			@Override
			protected Object visit(Assignment assignment) {
				env.assign(assignment.getVariableName(), visit(assignment.getExpression()));
				return null;
			}

			@Override
			protected Object visit(Block block) {
				env.enterBlock();
				Object returnValue = null;
				for (Statement s : block.getStatements()) {
					returnValue = visit(s);
					if(returnValue != null){
						env.exitBlock();
						return returnValue;
					}
				}
				env.exitBlock();
				return null;
			}

			@Override
			protected Object visit(ExpressionStatement expressionStatement) {
				visit(expressionStatement.getExpression());
				return null;
			}

			@Override
			protected Object visit(FunctionCall functionCall) {
				if(functionCall.isArithmeticOperation()){
					return visitArithmetic(functionCall);
				}
				else if(functionCall.isComparisonOperation()){
					return visitComparison(functionCall);
				}
				else if(env.get(functionCall.getFunctionName()) != null){
					FunctionDefinition function = (FunctionDefinition) env.get(functionCall.getFunctionName());
					Map<String, Object> newValues = new HashMap<>();
					for(int i = 0; i<function.getParameters().size(); i++){
						String name = function.getParameters().get(i).getVariableName();
						Object value = visit(functionCall.getArguments().get(i));
						newValues.put(name, value);
					}
					env.enterBlock();
					for(String k: newValues.keySet()){
						env.declare(k);
						env.assign(k, newValues.get(k));
					}
					Object returnValue = visit(function.getBody());
					env.exitBlock();
					return returnValue;
				}
				else {
					int functionArgumentsSize = functionCall.getArguments().size();
					String functionName = functionCall.getFunctionName();
					Object[] arguments = new Object[functionArgumentsSize];
					Class<?>[] types = new Class<?>[functionArgumentsSize];
					for (int i = 0; i < functionArgumentsSize; i++) {
						Object argument = visit(functionCall.getArguments().get(i));
						arguments[i] = argument;
						try {
							types[i] = argument.getClass();
						}catch (NullPointerException ex){
							System.out.println(argument);
							throw ex;
						}
					}
					try {

						Method method = AktkInterpreterBuiltins.class
								.getDeclaredMethod(functionName, types);
						Object returnValue = method.invoke(null, arguments);
						return returnValue;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}

			private Integer visitArithmetic(FunctionCall functionCall) {
				if(functionCall.getArguments().size() == 1){ //unaarne miinus
					return -1 * (Integer) visit(functionCall.getArguments().get(0));
				}
				Integer a1 = (Integer)visit(functionCall.getArguments().get(0));
				Integer a2 = (Integer)visit(functionCall.getArguments().get(1));
				assert functionCall.getArguments().size() == 2;
				switch (functionCall.getFunctionName()){
					case "+":
						return a1 + a2;
					case "-":
						return a1 - a2;
					case "*":
						return a1 * a2;
					case "/":
						return a1 / a2;
					case "%":
						return a1 % a2;
					default:
						throw new UnsupportedOperationException("Tundmatu aritmeetiline operaator: " + functionCall.getFunctionName());
				}
			}

			private Integer visitComparison(FunctionCall functionCall) {
				int a1 = (Integer)visit(functionCall.getArguments().get(0));
				int a2 = (Integer)visit(functionCall.getArguments().get(1));
				assert functionCall.getArguments().size() == 2;
				switch (functionCall.getFunctionName()){
					case "==":
						return a1 ==  a2 ? 1 : 0;
					case "!=":
						return a1 != a2 ? 1 : 0;
					case ">=":
						return a1 >= a2 ? 1 : 0;
					case "<=":
						return a1 <= a2 ? 1 : 0;
					case "<":
						return a1 < a2 ? 1 : 0;
					case ">":
						return a1 > a2 ? 1 : 0;
					default:
						throw new UnsupportedOperationException("Tundmatu aritmeetiline operaator: " + functionCall.getFunctionName());
				}
			}

			@Override
			protected Object visit(FunctionDefinition functionDefinition) {
				String name = functionDefinition.getName();
				env.declare(name);
				env.assign(name, functionDefinition);
				return null;
			}

			@Override
			protected Object visit(IfStatement ifStatement) {
				if((Integer)visit(ifStatement.getCondition()) > 0){
					return visit(ifStatement.getThenBranch());
				}else{
					return visit(ifStatement.getElseBranch());
				}
			}

			@Override
			protected Object visit(IntegerLiteral integerLiteral) {
				return integerLiteral.getValue();
			}

			@Override
			protected Object visit(ReturnStatement returnStatement) {
				return visit(returnStatement.getExpression());
			}

			@Override
			protected Object visit(StringLiteral stringLiteral) {
				return stringLiteral.getValue();
			}

			@Override
			protected Object visit(Variable variable) {
				return env.get(variable.getName());
			}

			@Override
			protected Object visit(VariableDeclaration variableDeclaration) {
				Object value = visit(variableDeclaration.getInitializer());
				env.declare(variableDeclaration.getVariableName());
				if(value != null) env.assign(variableDeclaration.getVariableName(), value);
				return null;
			}

			@Override
			protected Object visit(WhileStatement whileStatement) {
				while((Integer) visit(whileStatement.getCondition()) > 0){
					visit(whileStatement.getBody());
				}
				return null;
			}
		};
		visitor.visit(ast);
	}


}

class AktkEnvironment {

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
}

