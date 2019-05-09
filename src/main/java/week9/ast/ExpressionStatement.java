package week9.ast;

import java.util.Arrays;
import java.util.List;

/**
 * Avaldislause.
 *
 * Kasutatakse eeldatavasti väärtustamisel kõrvalefektiga avaldiste jaoks, mille väärtus pole oluline.
 * Näiteks 'print("tere")'.
 */
public class ExpressionStatement extends Statement {

	private final Expression expression;

	public ExpressionStatement(Expression expression) {
		this.expression = expression;
	}
	
	public Expression getExpression() {
		return expression;
	}

	@Override
	public List<Object> getChildren() {
		return Arrays.asList(expression);
	}

	@Override
	public <R> R accept(AstVisitor<R> visitor) {
		return visitor.visit(this);
	}
}
