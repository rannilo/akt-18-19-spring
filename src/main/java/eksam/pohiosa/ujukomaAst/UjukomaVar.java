package eksam.pohiosa.ujukomaAst;

import java.util.Objects;

public class UjukomaVar extends UjukomaNode {
    char name;

    public UjukomaVar(char name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UjukomaVar ujuVar = (UjukomaVar) o;
        return name == ujuVar.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


    @Override
    public String toString() {
        return "var('" + name + "')";
    }
}
