package week10;

import week9.AktkAst;
import week9.ast.*;

import java.lang.reflect.Method;
import java.util.*;

public class AktkInterpreter {
	private static final String RESULT_NAME = "__result__";

	public static void run(String program) {
		AstNode root = AktkAst.createAst(program);
		new AktkInterpeterVisitor().visit(root);
	}
	
	private static class AktkInterpeterVisitor extends AstVisitor<Object> {

		private final Environment env = new Environment();

		@Override
		protected Object visit(Assignment assignment) {
			String name = assignment.getVariableName();
			Object value = visit(assignment.getExpression());
			env.assign(name, value);

			return null;
		}

		@Override
		protected Object visit(Block block) {
			env.enterBlock();
			for (Statement stmt : block.getStatements()) {
				visit(stmt);
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
			String name = functionCall.getFunctionName();
			List<Object> argValues = new ArrayList<Object>();

			for (Expression arg : functionCall.getArguments()) {
				argValues.add(visit(arg));
			}

			if (name.equals("-") && argValues.size() == 1) {
				Number num = (Number) argValues.get(0);
				if (num instanceof Integer) {
					return - (Integer)num;
				}
				else {
					throw new RuntimeException("Unexpected number");
				}
			}
			else if (functionCall.isArithmeticOperation() || functionCall.isComparisonOperation()) {
				return callOperation(name, argValues);
			}
			else if (env.has(name)) {
				return callAKTKFunction((FunctionDefinition) env.get(name), argValues);
			}
			else {
				return callBuiltinFunction(name, argValues);
			}
		}


		private Object callOperation(String name, List<Object> argValues) {
			if (argValues.size() != 2) {
				throw new RuntimeException("wrong number of args");
			}
			Object left = argValues.get(0);
			Object right = argValues.get(1);
			if (left instanceof String && right instanceof String) {
				String sLeft = (String) left;
				String sRight = (String) right;
				switch (name) {
					case "+": return sLeft + sRight;
					case "==": return bool2Int(sLeft.equals(sRight));
					case "!=": return bool2Int(!sLeft.equals(sRight));
					case "<": return bool2Int(sLeft.compareTo(sRight) < 0);
					case ">": return bool2Int(sLeft.compareTo(sRight) > 0);
					case "<=": return bool2Int(sLeft.compareTo(sRight) <= 0);
					case ">=": return bool2Int(sLeft.compareTo(sRight) >= 0);
					default: throw new RuntimeException("unexpected operator");
				}
			}
			else if (left instanceof Integer && right instanceof Integer) {
				Integer iLeft = (Integer) left;
				Integer iRight = (Integer) right;
				switch (name) {
					case "+": return iLeft + iRight;
					case "-": return iLeft - iRight;
					case "*": return iLeft * iRight;
					case "/": return iLeft / iRight;
					case "%": return iLeft % iRight;
					case "==": return bool2Int(iLeft.equals(iRight));
					case "!=": return bool2Int(!iLeft.equals(iRight));
					case "<": return bool2Int(iLeft < iRight);
					case ">": return bool2Int(iLeft > iRight);
					case "<=": return bool2Int(iLeft <= iRight);
					case ">=": return bool2Int(iLeft >= iRight);
					default: throw new RuntimeException("unexpected operator");
				}
			}
			else {
				throw new RuntimeException("unexpected operator");
			}
		}

		private Object callBuiltinFunction(String name, List<Object> argValues) {

			// arg-list description
			Class<?>[] argClasses = new Class[argValues.size()];
			Object[] argValuesArray = new Object[argValues.size()];
			for (int i=0; i < argValues.size(); i++) {
				argClasses[i] = argValues.get(i).getClass();
				argValuesArray[i] = argValues.get(i);
			}

			try {
				Method method = AktkInterpreterBuiltins.class.getDeclaredMethod(name, argClasses);
				return method.invoke(null, argValuesArray);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private Object callAKTKFunction(FunctionDefinition def, List<Object> argValues) {
			env.enterBlock();
			env.declare(RESULT_NAME, null);
			if (argValues.size() != def.getParameters().size()) {
				throw new RuntimeException("Wrong number of args");
			}

			// set up environment
			for (int i=0; i < argValues.size(); i++) {
				env.declare(def.getParameters().get(i).getVariableName(), argValues.get(i));
			}

			visit(def.getBody());

			Object result = env.get(RESULT_NAME);
			env.exitBlock();
			return result;
		}

		@Override
		protected Object visit(FunctionDefinition functionDefinition) {
			env.declare(functionDefinition.getName(), functionDefinition);

			return null;
		}

		@Override
		protected Object visit(IfStatement ifStatement) {
			Object test = visit(ifStatement.getCondition());
			if (test.equals(0)) {
				if (ifStatement.getElseBranch() != null) {
					visit(ifStatement.getElseBranch());
				}
			} else {
				visit(ifStatement.getThenBranch());
			}

			return null;
		}

		@Override
		protected Object visit(IntegerLiteral integerLiteral) {
			return integerLiteral.getValue();
		}

		@Override
		protected Object visit(ReturnStatement returnStatement) {
			Object value = visit(returnStatement.getExpression());
			env.assign(RESULT_NAME, value);

			return null;
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
			if (variableDeclaration.getInitializer() != null) {
				env.declare(variableDeclaration.getVariableName(), visit(variableDeclaration.getInitializer()));
			}
			else {
				env.declare(variableDeclaration.getVariableName(), null);
			}

			return null;
		}

		@Override
		protected Object visit(WhileStatement whileStatement) {
			while (!visit(whileStatement.getCondition()).equals(0)) {
				visit(whileStatement.getBody());
			}

			return null;
		}
	}

	private static int bool2Int(boolean value) {
		return value ? 1 : 0;
	}

    private static class Environment {
        private Deque<Map<String, Object>> namespaces = new LinkedList<>();

        public void enterBlock() {
            namespaces.push(new HashMap<>());
        }

        public void exitBlock() {
            namespaces.pop();
        }

        public Object get(String name) {
            return find(name).get(name);
        }

        public boolean has(String name) {
            try {
                get(name);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        public void assign(String name, Object value) {
            find(name).put(name, value);
        }

        public void declare(String name, Object value) {
            namespaces.peek().put(name, value);
        }

        private Map<String, Object> find(String name) {
            for (Map<String, Object> block : namespaces) {
                if (block.containsKey(name)) {
                    return block;
                }
            }

            throw new RuntimeException("Name "+name+" not found");
        }
    }
}

