package week5.regex;

public class Epsilon extends RegexNode {
    public Epsilon() {
        super('ε');
    }

    @Override
    public <R> R accept(RegexVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
