package week9.ast;

import java.util.Arrays;
import java.util.List;

/**
 * if-lause.
 */
public class IfStatement extends Statement {
	
	private final Expression condition;
	private final Statement thenBranch;
	private final Statement elseBranch;

	public IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) {
		this.condition = condition;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	public Expression getCondition() {
		return condition;
	}

	public Statement getThenBranch() {
		return thenBranch;
	}

	public Statement getElseBranch() {
		return elseBranch;
	}

	@Override
	public List<Object> getChildren() {
		return Arrays.asList(condition, thenBranch, elseBranch);
	}

	@Override
	public <R> R accept(AstVisitor<R> visitor) {
		return visitor.visit(this);
	}
}
