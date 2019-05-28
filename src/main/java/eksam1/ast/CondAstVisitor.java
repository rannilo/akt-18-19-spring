package eksam1.ast;

public class CondAstVisitor<T> {

    protected T visit(CondLitInt num) {
        return null;
    }

    protected T visit(CondLitBool tv) {
        return null;
    }

    protected T visit(CondVar var) {
        return null;
    }

    protected T visit(CondUnary unary)  {
        return visit(unary.getExpr());
    }

    protected T visit(CondBinary binary)  {
        visit(binary.getLeftExpr());
        return visit(binary.getRightExpr());
    }

    protected T visit(CondTernary ifte) {
        visit(ifte.getGuardExpr());
        visit(ifte.getTrueExpr());
        return visit(ifte.getFalseExpr());
    }

    protected T visit(CondDecl decl) {
        return null;
    }

    protected T visit(CondProg prog) {
        for (CondDecl decl : prog.getDecls()) visit(decl);
        return visit(prog.getExpression());
    }

    public final T visit(CondNode node) {
        return node.accept(this);
    }


}
