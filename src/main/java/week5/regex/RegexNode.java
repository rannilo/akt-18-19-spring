package week5.regex;

import java.util.Arrays;
import java.util.List;

/**
 * Regulaaravaldise süntakspuu tipude ülemklass. Annab ka homogeense vaade ASTile.
 */

public abstract class RegexNode {
    public final char type;
    public final List<RegexNode> children;
    

    public RegexNode(char type, RegexNode... children) {
        this.type = type;
        this.children = Arrays.asList(children);
    }


    public String toString() {
        if (children.isEmpty()) {
            return Character.toString(type);
        }
        StringBuilder buf = new StringBuilder();
        buf.append(type).append("(");
        boolean first = true;
        for (RegexNode child : children) {
            if (!first) buf.append(',');
            first = false;
            buf.append(child);
        }
        buf.append(")");
        return buf.toString();
    }
    public List<RegexNode> getChildren() {
        return children;
    }

    public RegexNode getChild(int i) {
        return children.get(i);
    }
    
    @Override
    public boolean equals(Object that) {
    	return this.toString().equals(that.toString());
    }
    
    @Override
    public int hashCode() {
    	return this.toString().hashCode();
    }

    public abstract <R> R accept(RegexVisitor<R> visitor);
    
}


