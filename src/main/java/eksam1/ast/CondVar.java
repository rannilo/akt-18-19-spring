package eksam1.ast;

public class CondVar extends CondNode {
    private String name;

    public CondVar(String name) {
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
    public <T> T accept(CondAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
