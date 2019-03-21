package week5.alusosa.bexp.bexpAst;

public class Var extends BExp {
    private final char name;

    public Var(char name) {
        this.name = name;
    }

    public char getName() {
        return name;
    }

    @Override
    public <T> T accept(BExpVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "var(\'" + name + "\')";
    }
}
