package week9.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Funktsiooni väljakutse.
 *
 * Kasutatakse ka operaatorite tähistamiseks (selleks eraldi klassi pole).
 *
 * Infiksoperaatorite korral peaks argumentideks olema kaks operandi. Näiteks avaldise '2 + 3' AST oleks
 * {@code new FunctionCall("+", new IntegerLiteral(2), new IntegerLiteral(3))}.
 *
 * Unaarse miinuse korral peaks argumendiks olema ainus operand. Näiteks avaldise '-2' AST oleks
 * {@code new FunctionCall("-", new IntegerLiteral(2))}.
 */
public class FunctionCall extends Expression {
	
	private final String functionName;
	private final List<Expression> arguments;

	public FunctionCall(String functionName, List<Expression> arguments) {
		this.functionName = functionName;
		this.arguments = arguments;
	}

	public FunctionCall(String functionName, Expression... arguments) {
		this(functionName, Arrays.asList(arguments));
	}

	public String getFunctionName() {
		return functionName;
	}

	public List<Expression> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	@Override
	public List<Object> getChildren() {
		List<Object> children = new ArrayList<>();
		children.add(functionName);
		children.addAll(arguments);
		return children;
	}

	public boolean isComparisonOperation() {
		return Arrays.asList(">", "<", ">=", "<=", "==", "!=").contains(functionName);
	}

	public boolean isArithmeticOperation() {
		return Arrays.asList("+", "-", "*", "/", "%").contains(functionName);
	}
}
