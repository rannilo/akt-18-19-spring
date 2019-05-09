package week9.ast;

import java.util.Arrays;
import java.util.List;

/**
 * Muutujaviide.
 *
 * Kasutatakse ainult avaldistes, aga mitte omistuslausete vasakutes pooltes.
 */
public class Variable extends Expression {
	
	private final String name;

	/**
	 * Sidumine viidatava muutujaga.
	 */
	private VariableBinding binding = null;

	public Variable(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public VariableBinding getBinding() {
		return binding;
	}

	public void setBinding(VariableBinding binding) {
		this.binding = binding;
	}

	@Override
	public List<Object> getChildren() {
		return Arrays.asList(name);
	}

	@Override
	public <R> R accept(AstVisitor<R> visitor) {
		return visitor.visit(this);
	}
}
