package week9.pohiosa.letex.letAst;

import java.util.Objects;

/**
 * Täisarv.
 */
public class LetArv extends LetAvaldis {
    private int arv;

    public LetArv(int arv) {
        this.arv = arv;
    }

    public int getArv() {
        return arv;
    }

    @Override
    public String toString() {
        return "num(" + getArv() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LetArv letArv = (LetArv) o;
        return arv == letArv.arv;
    }

    @Override
    public int hashCode() {

        return Objects.hash(arv);
    }

    @Override
    public <T> T accept(LetVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
