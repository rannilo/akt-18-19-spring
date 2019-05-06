package eksam.pohiosa.ujukomaAst;

import java.util.Objects;

public class UjukomaBinop extends UjukomaNode {

    char op;
    UjukomaNode left;
    UjukomaNode right;

    public UjukomaBinop(char op, UjukomaNode left, UjukomaNode right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UjukomaBinop ujuBinop = (UjukomaBinop) o;
        return op == ujuBinop.op &&
                Objects.equals(left, ujuBinop.left) &&
                Objects.equals(right, ujuBinop.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(op, left, right);
    }

    @Override
    public String toString() {
        return String.format("op('%s', %s, %s)", op, left, right);
    }

}
