package eksam1.ast;

public class CondDecl extends CondNode {

    private String name;
    private boolean intType;

    public CondDecl(String name, boolean intType) {
        this.name = name;
        this.intType = intType;
    }

    public String getName() {
        return name;
    }

    public boolean isIntType() {
        return intType;
    }

    @Override
    public String toString() {
        String tcon = intType ? "iv" : "bv";
        return String.format("%s(\"%s\")", tcon, name);
    }

    @Override
    public <T> T accept(CondAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
