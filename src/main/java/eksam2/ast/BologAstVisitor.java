package eksam2.ast;

public class BologAstVisitor<T> {

    protected T visit(BologLit tv) {
        return null;
    }

    protected T visit(BologVar var) {
        return null;
    }

    protected T visit(BologNand nand)  {
        visit(nand.getLeftExpr());
        return visit(nand.getRightExpr());
    }

    protected T visit(BologImp imp) {
        for (BologNode assumption : imp.getAssumptions()) {
            visit(assumption);
        }
        return visit(imp.getConclusion());
    }

    public final T visit(BologNode node) {
        return node.accept(this);
    }

}
