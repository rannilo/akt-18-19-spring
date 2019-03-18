package week5.regex;

public abstract class RegexVisitor<R> {
    protected abstract R visit(Letter letter);
    protected abstract R visit(Epsilon epsilon);
    protected abstract R visit(Repetition repetition);
    protected abstract R visit(Concatenation concatenation);
    protected abstract R visit(Alternation alternation);

    public R visit(RegexNode regexNode) {
        return regexNode.accept(this);
    }

}
