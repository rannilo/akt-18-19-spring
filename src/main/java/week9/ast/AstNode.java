package week9.ast;

import org.apache.commons.text.StringEscapeUtils;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * AKTK abstraktse süntaksipuu tippude ülemklass.
 */
public abstract class AstNode {

    /**
     * Tagastab kõik tipu alamad.
     * Lisaks teistele AstNode'idele võib sisaldada ka muud tüüpi väärtusi ja {@code null}-e.
     */
	public abstract List<Object> getChildren();

    @Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", getSimpleName() + "(", ")");
		for (Object child : getChildren()) {
			String childString;
			if (child instanceof String)
				childString = "\"" + StringEscapeUtils.escapeJava((String) child) + "\"";
			else
				childString = Objects.toString(child); // child may be null
			joiner.add(childString);
		}
		return joiner.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		AstNode that = (AstNode) obj;
		return Objects.equals(this.getChildren(), that.getChildren());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSimpleName(), getChildren());
	}

	private String getSimpleName() {
		return getClass().getSimpleName();
	}

	public abstract <R> R accept(AstVisitor<R> visitor);

	/*
    protected void dotAddAttributes(StringBuilder out) {
        return;
    }

    private void dotHelper(StringBuilder out) {
        out.append(myID).append(" [label = \"").append(getSimpleName()).append("\", ");
        dotAddAttributes(out);
        out.append("]\n");
        for (Object child : getChildren()) {
            out.append(myID).append(" -> ").append(child.myID).append("\n");
            child.dotHelper(out);
        }
    }

    public String toDotString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph AST {\n");
        sb.append("bgcolor=transparent;\n");
        sb.append("node [style=filled, shape=circle, fixedsize=true];\n");
        dotHelper(sb);
        sb.append("}\n");
        return sb.toString();
    }
    */
}
