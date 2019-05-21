package week9.pohiosa.letex.letAst;

import java.util.Objects;

/**
 * Lahutamine.
 */
public class LetVahe extends LetAvaldis {
    private LetAvaldis vasak;
    private LetAvaldis parem;

    public LetVahe(LetAvaldis vasak, LetAvaldis parem) {
        this.vasak = vasak;
        this.parem = parem;
    }


    public LetAvaldis getVasak() {
        return vasak;
    }

    public LetAvaldis getParem() {
        return parem;
    }

    @Override
    public String toString() {
        return "vahe(" + getVasak() + "," + getParem() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LetVahe letVahe = (LetVahe) o;
        return Objects.equals(vasak, letVahe.vasak) &&
                Objects.equals(parem, letVahe.parem);
    }

    @Override
    public int hashCode() {

        return Objects.hash(vasak, parem);
    }

    @Override
    public <T> T accept(LetVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
