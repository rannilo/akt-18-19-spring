package week9.ast;

import java.util.Arrays;
import java.util.List;

/**
 * Funktsiooni parameeter funktiooni definitsioonis.
 *
 * @see FunctionDefinition
 */
public class FunctionParameter extends AstNode implements VariableBinding {

	private final String parameterName;
	private final String type;

	public FunctionParameter(String parameterName, String type) {
		this.parameterName = parameterName;
        this.type = type;
    }
	
	@Override
	public String getVariableName() {
		return parameterName;
	}

    @Override
    public String getType() {
        return type;
    }

	@Override
	public List<Object> getChildren() {
		return Arrays.asList(parameterName, type);
	}

	@Override
	public <R> R accept(AstVisitor<R> visitor) {
		return visitor.visit(this);
	}
}
