package week9.pohiosa.letex.letAst;

import java.util.Objects;

/**
 * Vaba muutuja, mis saab võtta täisarvulisi väärtuseid.
 */
public class LetMuutuja extends LetAvaldis {
    private String nimi;

    public LetMuutuja(String nimi) {
        this.nimi = nimi;
    }

    public String getNimi() {
        return nimi;
    }

    @Override
    public String toString() {
        return "var(\"" + getNimi() + "\")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LetMuutuja that = (LetMuutuja) o;
        return Objects.equals(nimi, that.nimi);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nimi);
    }

    @Override
    public <T> T accept(LetVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
