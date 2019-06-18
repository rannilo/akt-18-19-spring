package week11;

import week10.AktkInterpreterBuiltins;
import week9.ast.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;


public class AktkTypeChecker {

    public static void check(AstNode ast) {
        AktkBinding.bind(ast);
        //visitor
        //tagastus string (tüüp)
        // Meetod peab viskama RuntimeException-i (või mõne selle alamklassi erindi), kui:
        // 1) programmis kasutatakse deklareerimata muutujaid või funktsioone,
        // mida pole defineeritud ei antud programmis ega "standardteegis"
        //    (vt. interpretaatori koduülesannet)
        // 2) programmis kasutatakse mõnd lihttüüpi, mis pole ei String ega Integer
        // 3) leidub muutuja deklaratsioon, milles pole antud ei tüüpi ega algväärtusavaldist
        // 4) programmis on mõnel avaldisel vale tüüp
        AstVisitor<String> visitor = new AstVisitor<String>() {
            @Override
            protected String visit(Assignment assignment) {
                String expectedType = assignment.getBinding().getType();
                if (expectedType != null) {
                    String actualType = visit(assignment.getExpression());
                    if (!expectedType.equals(actualType)) throw new RuntimeException("Vale tüüp omistamisel");
                }
                return null;
            }

            @Override
            protected String visit(Block block) {
                for (Statement s : block.getStatements()) {
                    visit(s);
                }
                return null;
            }

            @Override
            protected String visit(ExpressionStatement expressionStatement) {
                visit(expressionStatement.getExpression());
                return null;
            }

            @Override
            protected String visit(FunctionCall functionCall) {
                if (functionCall.isArithmeticOperation()) {
                    for (Expression e : functionCall.getArguments()) {
                        String type = visit(e);
                        if (!type.equals("Integer"))
                            throw new RuntimeException("Aritmeetilise operatsiooni tüüp peab olema Integer");
                    }
                    return "Integer";
                }
                else if (functionCall.isComparisonOperation()) {
                    if (functionCall.getArguments().size() != 2)
                        throw new RuntimeException("Võrdlusoperatsioonis peab olema kaks argumenti");
                    String type = visit(functionCall.getArguments().get(0));
                    String type2 = visit(functionCall.getArguments().get(1));
                    if (!type.equals(type2))
                        throw new RuntimeException("Võrdlusoperatsioonis peavad olema argumendid sama tüüpi");
                    return "Integer";
                }
                else if(functionCall.getFunctionBinding() != null){
                    List<FunctionParameter> expectedParameters = functionCall.getFunctionBinding().getParameters();
                    List<Expression> actualParameters = functionCall.getArguments();
                    if(expectedParameters.size() != actualParameters.size()) throw new RuntimeException();
                    for(int i = 0; i<expectedParameters.size(); i++){
                        String expectedType = expectedParameters.get(i).getType();
                        String actualType = visit(actualParameters.get(i));
                        if(!expectedType.equals(actualType)) throw new RuntimeException();
                    }
                    return functionCall.getFunctionBinding().getReturnType();
                }
                else{
                    String functionName = functionCall.getFunctionName();
                    int functionArgumentsSize = functionCall.getArguments().size();
                    Class<?>[] types = new Class<?>[functionArgumentsSize];
                    for (int i = 0; i<functionArgumentsSize; i++){
                        String type = visit(functionCall.getArguments().get(i));
                        if(type.equals("Integer")) types[i] = Integer.valueOf(2).getClass();
                        else if(type.equals("String")) types[i] = "tekst".getClass();
                        else types[i] = null;
                    }
                    try {
                        Method method = AktkInterpreterBuiltins.class.getDeclaredMethod(functionName, types);
                        if(method.getReturnType().toString().contains("String")) return "String";
                        if(method.getReturnType().toString().contains("Integer")) return "Integer";
                    }catch (NoSuchMethodException e){
                        throw new RuntimeException("Selline meetod puudub");
                    }
                }
                return null;
            }

            @Override
            protected String visit(FunctionDefinition functionDefinition) {
                visit(functionDefinition.getBody());
                return null;
            }

            @Override
            protected String visit(IfStatement ifStatement) {
                if(!visit(ifStatement.getCondition()).equals("Integer")) throw new RuntimeException();
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
                String expectedType = returnStatement.getFunctionBinding().getReturnType();
                String actualType = visit(returnStatement.getExpression());
                if(!expectedType.equals(actualType)) throw new RuntimeException();
                return null;
            }

            @Override
            protected String visit(StringLiteral stringLiteral) {
                return "String";
            }

            @Override
            protected String visit(Variable variable) {
                VariableBinding variableBinding = variable.getBinding();
                if (variableBinding == null) throw new RuntimeException();
                String type = variable.getBinding().getType();
                if (type.equals("Integer") || type.equals("String")) return type;
                throw new RuntimeException("Muutuja tüüp ei ole Integer ega String");
            }

            @Override
            protected String visit(VariableDeclaration variableDeclaration) {
                if (variableDeclaration.getType() == null) {
                    if (variableDeclaration.getInitializer() == null) {
                        throw new RuntimeException("Algväärtuseta deklareerides peab olema defineeritud tüüp");
                    }
                    else{
                        variableDeclaration.setType(visit(variableDeclaration.getInitializer()));
                    }
                }
                if (variableDeclaration.getType() != null) {
                    String expectedType = variableDeclaration.getType();
                    if(variableDeclaration.getInitializer() != null) {
                        String actualType = visit(variableDeclaration.getInitializer());
                        if (!expectedType.equals(actualType)) {
                            throw new RuntimeException("Muutuja deklareerimisel peab muutuja tüüp olema õige.");
                        }
                    }
                }
                return null;
            }

            @Override
            protected String visit(WhileStatement whileStatement) {
                if(!visit(whileStatement.getCondition()).equals("Integer")) throw new RuntimeException();
                visit(whileStatement.getBody());
                return null;
            }
        };
        visitor.visit(ast);
    }
}
