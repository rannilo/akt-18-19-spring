package week11;

import week10.AktkInterpreterBuiltins;
import week9.ast.*;

import java.lang.reflect.Method;


public class AktkTypeChecker {

	public static void check(AstNode ast) {
        // Meetod peab viskama RuntimeException-i (või mõne selle alamklassi erindi), kui:
        // 1) programmis kasutatakse deklareerimata muutujaid või funktsioone,
        // mida pole defineeritud ei antud programmis ega "standardteegis"
        //    (vt. interpretaatori koduülesannet)
        // 2) programmis kasutatakse mõnd lihttüüpi, mis pole ei String ega Integer
        // 3) leidub muutuja deklaratsioon, milles pole antud ei tüüpi ega algväärtusavaldist
        // 4) programmis on mõnel avaldisel vale tüüp
		AktkBinding.bind(ast);
		new AktkTypeCheckerVisitor().visit(ast);
	}

	private static class AktkTypeCheckerVisitor extends AstVisitor<String> {
		@Override
		protected String visit(Assignment assignment) {
			if (assignment.getBinding() == null) {
				throw new RuntimeException();
			}
			String exprType = visit(assignment.getExpression());
			String varType = assignment.getBinding().getType();

			if (!varType.equals(exprType)) {
				throw new RuntimeException();
			}

			return null;
		}

		@Override
		protected String visit(Block block) {
			for (Statement statement : block.getStatements()) {
				visit(statement);
			}

			return null;
		}

		@Override
		protected String visit(ExpressionStatement expressionStatement) {
			// meid ei huvita avaldise tüüp, aga vaja siiski kontrollida, et
			// avaldise sees pole vastuolusid
			visit(expressionStatement.getExpression());

			return null;
		}

		@Override
		protected String visit(FunctionCall functionCall) {
			String integerType = "Integer";
			String stringType = "String";

			if (!functionCall.isArithmeticOperation() && !functionCall.isComparisonOperation()) {
				if (functionCall.getFunctionBinding() == null) {
					// builtin

					// kontrollin kas argumentide tüübid klapivad
					Class<?>[] argClasses = new Class<?>[functionCall.getArguments().size()];
					for (int i = 0; i < functionCall.getArguments().size(); i++) {
						String argType = visit(functionCall.getArguments().get(i));
						argClasses[i] = getAKTKTypeClass(argType);
					}

					try {
						Method method = AktkInterpreterBuiltins.class.getMethod(functionCall.getFunctionName(), argClasses);
						return getAKTKSimpleTypeName(method.getReturnType());
					}
					catch (NoSuchMethodException e) {
						throw new RuntimeException("Can't find function '" + functionCall.getFunctionName() + '"', e);
					}
				}
				else {
					// kontrollin argumentide arvu
					FunctionDefinition fun = functionCall.getFunctionBinding();
					if (fun.getParameters().size() != functionCall.getArguments().size()) {
						throw new RuntimeException();
					}

					// kontrollin kas argumentide tüübid klapivad
					for (int i = 0; i < functionCall.getArguments().size(); i++) {
						FunctionParameter param = fun.getParameters().get(i);
						String argType = visit(functionCall.getArguments().get(i));
						if (!param.getType().equals(argType)) {
							throw new RuntimeException();
						}
					}

					// kõik kontrollid tehtud
					return fun.getReturnType();
				}
			}
			else if (functionCall.getFunctionName().equals("-") && functionCall.getArguments().size() == 1) {
				// unaarne miinus
				String String = visit(functionCall.getArguments().get(0));
				if (String.equals(integerType)) {
					return String;
				}
				else {
					throw new RuntimeException();
				}
			}
			else {
				assert functionCall.getArguments().size() == 2;
				String leftType = visit(functionCall.getArguments().get(0));
				String rightType = visit(functionCall.getArguments().get(1));


				if (functionCall.isArithmeticOperation()) {
					if (leftType.equals(integerType) && rightType.equals(integerType)) {
						return integerType;
					}
					else if (functionCall.getFunctionName().equals("+")
							&& leftType.equals(stringType) && rightType.equals(stringType)) {
						return stringType;
					}
					else {
						throw new RuntimeException();
					}
				}
				else if (functionCall.isComparisonOperation()){
					if ((leftType.equals(integerType)
							|| leftType.equals(stringType))
						&& leftType.equals(rightType)) {
						return integerType;
					}
					else {
						throw new RuntimeException();
					}
				}
				else {
					// siia ei tohiks jõuda
					throw new RuntimeException("Unexpected kind of function call");
				}
			}
		}

		@Override
		protected String visit(FunctionDefinition functionDefinition) {
			visit(functionDefinition.getBody());

			return null;
		}

		@Override
		protected String visit(IfStatement ifStatement) {
			String conditionType = visit(ifStatement.getCondition());
			if (!conditionType.equals("Integer")) {
				throw new RuntimeException();
			}
			visit(ifStatement.getThenBranch());
			visit(ifStatement.getElseBranch());

			return null;
		}

		@Override
		protected String visit(IntegerLiteral integerLiteral) {
			return "Integer";
		}

		@Override
		protected String visit(ReturnStatement returnStatement) {
			Expression exp = returnStatement.getExpression();
			String actualReturnType = visit(exp);
			String declaredReturnType = returnStatement.getFunctionBinding().getReturnType();
			if (!actualReturnType.equals(declaredReturnType)) {
				throw new RuntimeException("Actual and declared return don't match");
			}

			return null;
		}

		@Override
		protected String visit(StringLiteral stringLiteral) {
			return "String";
		}

		@Override
		protected String visit(Variable variable) {
			String name = variable.getName();
			if (variable.getBinding() == null) {
				throw new RuntimeException("Can't find variable '" + name + '"');
			}
			else {
				return variable.getBinding().getType();
			}
		}

		@Override
		protected String visit(VariableDeclaration variableDeclaration) {
			// Muutuja deklaratsioonis peab olema kas tüüp või algväärtusavaldis või mõlemad.
			// Kui on mõlemad, siis nende tüübid peavad klappima.

			// Algväärtusavaldis ei näe seda muutujat, mida parasjagu deklareeritakse
			// st. lause var x = x + 1 omab mõtet vaid siis, kui ka mõnes ülevalpool olevas skoobis
			// on deklareeritud muutuja x.

			String declaredType = variableDeclaration.getType();
			String initializerType = null;
			String varType = null;

			if (variableDeclaration.getInitializer() != null) {
				initializerType = visit(variableDeclaration.getInitializer());
			}

			if (declaredType == null && initializerType == null) {
				throw new RuntimeException();
			}
			else if (declaredType != null && !isKnownType(declaredType)) {
				throw new RuntimeException("Unknown type: " + declaredType + variableDeclaration);
			}
			else if (declaredType != null && initializerType != null
					&& !declaredType.equals(initializerType)) {
				throw new RuntimeException();
			}
			else if (declaredType != null) {
				varType = declaredType;
			}
			else {
				varType = initializerType;
			}

			variableDeclaration.setType(varType);

			return null;
		}

		@Override
		protected String visit(WhileStatement whileStatement) {
			String conditionType = visit(whileStatement.getCondition());
			if (!conditionType.equals("Integer")) {
				throw new RuntimeException();
			}
			visit(whileStatement.getBody());

			return null;
		}
	}

    private static boolean isKnownType(String type) {
		return type.equals("String") || type.equals("Integer");
	}

	private static String getAKTKSimpleTypeName(Class<?> javaClass) {
    	if (javaClass == Integer.class) {
    		return "Integer";
    	}
    	else if (javaClass == void.class) {
    		return "Void";
    	}
    	else if (javaClass == String.class) {
    		return "String";
    	}
    	else {
    		throw new UnsupportedOperationException("Unsupported Java type " + javaClass);
    	}
    }

    private static Class<?> getAKTKTypeClass(String aktkType) {
		switch (aktkType) {
			case "Integer":
				return Integer.class;
			case "String":
				return String.class;
			case "Void":
				return void.class;
			default:
				throw new UnsupportedOperationException("Unsupported AKTK type " + aktkType);
		}
    }
}
