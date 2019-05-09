package week9.ast;

import java.util.Arrays;
import java.util.List;

/**
 * Omistuslause.
 */
public class Assignment extends Statement {

	private final String variableName;
	private final Expression expression;

	public Assignment(String variableName, Expression expression) {
		this.variableName = variableName;
		this.expression = expression;
	}

	public String getVariableName() {
		return variableName;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public List<Object> getChildren() {
		return Arrays.asList(variableName, expression);
	}

	@Override
	public <R> R accept(AstVisitor<R> visitor) {
		return visitor.visit(this);
	}
}
