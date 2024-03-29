package week9.ast;

import java.util.Arrays;
import java.util.List;

/** 
 * while-lause.
 */
public class WhileStatement extends Statement {
	
	private final Expression condition;
	private final Statement body;

	public WhileStatement(Expression condition, Statement body) {
		this.condition = condition;
		this.body = body;
	}

	public Expression getCondition() {
		return condition;
	}

	public Statement getBody() {
		return body;
	}

	@Override
	public List<Object> getChildren() {
		return Arrays.asList(condition, body);
	}

	@Override
	public <R> R accept(AstVisitor<R> visitor) {
		return visitor.visit(this);
	}
}
