package eksam.alusosa.nondetAst;

public abstract class NdVisitor<T> {

    protected abstract T visit(NdNum num);
    protected abstract T visit(NdInc inc);
    protected abstract T visit(NdMul mul);
    protected abstract T visit(NdChoice choice);

    public T visit(NdExpr node) {
        return node.accept(this);
    }

}
