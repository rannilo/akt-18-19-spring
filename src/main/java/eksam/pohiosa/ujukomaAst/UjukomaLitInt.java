package eksam.pohiosa.ujukomaAst;

import java.util.Objects;

public class UjukomaLitInt extends UjukomaNode {
    private int value;

    public UjukomaLitInt(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UjukomaLitInt ujuLitInt = (UjukomaLitInt) o;
        return value == ujuLitInt.value;
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
