package eksam.pohiosa.ujukomaAst;

import java.util.Objects;

public class UjukomaLitDouble extends UjukomaNode {
    private double value;

    public UjukomaLitDouble(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UjukomaLitDouble ujuLitDouble = (UjukomaLitDouble) o;
        return value == ujuLitDouble.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }


    @Override
    public String toString() {
        return "lit(" + value + ')';
    }
}
