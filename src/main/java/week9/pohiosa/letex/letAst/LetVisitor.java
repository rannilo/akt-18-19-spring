package week9.pohiosa.letex.letAst;

public abstract class LetVisitor<T> {

    protected abstract T visit(LetArv arv);
    protected abstract T visit(LetMuutuja muutuja);
    protected abstract T visit(LetSidumine sidumine);
    protected abstract T visit(LetSumma summa);
    protected abstract T visit(LetVahe vahe);

    public T visit(LetAvaldis avaldis) {
        return avaldis.accept(this);
    }
}
