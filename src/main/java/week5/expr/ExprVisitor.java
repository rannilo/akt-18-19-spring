package week5.expr;

public abstract class ExprVisitor<T> {

    protected abstract T visit(Num num);
    protected abstract T visit(Neg neg);
    protected abstract T visit(Div div);
    protected abstract T visit(Add add);

    public T visit(ExprNode node) {
        return node.accept(this);
    }


    public static class BaseVisitor<T> extends ExprVisitor<T> {

        @Override
        protected T visit(Num num) {
            return null;
        }

        @Override
        protected T visit(Neg neg) {
            return visit(neg.getExpr());
        }

        @Override
        protected T visit(Div div) {
            return aggregateResult(visit(div.getNumerator()), visit(div.getDenominator()));
        }

        @Override
        protected T visit(Add add) {
            return aggregateResult(visit(add.getLeft()), visit(add.getRight()));
        }

        protected T aggregateResult(T aggregate, T nextResult) {
            return nextResult;
        }
    }
}
