package week5.alusosa.bexp.bexpAst;

public abstract class BExpVisitor<T> {

    protected abstract T visit(Imp node);
    protected abstract T visit(Or node);
    protected abstract T visit(Not node);
    protected abstract T visit(Var node);

    public final T visit(BExp node) {
        return node.accept(this);
    }

}
