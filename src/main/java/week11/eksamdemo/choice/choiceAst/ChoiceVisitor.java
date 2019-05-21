package week11.eksamdemo.choice.choiceAst;

public abstract class ChoiceVisitor<T> {

    protected abstract T visit(ChoiceValue value);
    protected abstract T visit(ChoiceAdd add);
    protected abstract T visit(ChoiceDecision decision);

    public T visit(ChoiceNode node) {
        return node.accept(this);
    }

}
