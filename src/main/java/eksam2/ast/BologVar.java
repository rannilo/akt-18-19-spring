package eksam2.ast;

public class BologVar extends BologNode {
    private String name;

    public BologVar(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "var(\"" + name + "\")";
    }

    @Override
    public <T> T accept(BologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
