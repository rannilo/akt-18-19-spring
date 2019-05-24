package week11.eksamdemo.funlist.ast;

public class FunlistAstVisitor<T> {

    protected T visit(FunlistLit lit) {
        return null;
    }

    protected T visit(FunlistVar var) {
        return null;
    }

    protected T visit(FunlistAdd add) {
        visit(add.getLeft());
        return visit(add.getRight());
    }
    protected T visit(FunlistFun fun)  {
        return visit(fun.getBody());
    }

    protected T visit(FunlistProg prog) {
        T res = null;
        for (FunlistFun fun : prog.getFuns()) res = fun.accept(this);
        return res;
    }

    public T visit(FunlistNode node) {
        return node.accept(this);
    }

}
