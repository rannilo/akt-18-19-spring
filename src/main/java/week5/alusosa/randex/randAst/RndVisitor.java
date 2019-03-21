package week5.alusosa.randex.randAst;

public abstract class RndVisitor<T> {

    protected abstract T visit(RndNum num);
    protected abstract T visit(RndNeg neg);
    protected abstract T visit(RndFlip flip);
    protected abstract T visit(RndAdd add);

    public T visit(RndExpr node) {
        return node.accept(this);
    }

}
