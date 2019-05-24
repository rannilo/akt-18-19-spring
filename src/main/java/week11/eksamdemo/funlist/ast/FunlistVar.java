package week11.eksamdemo.funlist.ast;

public class FunlistVar extends FunlistNode {
    private final Character name;

    public FunlistVar(Character name) {
        this.name = name;
    }

    @Override
    public <T> T accept(FunlistAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Character getName() {
        return name;
    }


    @Override
    public String toString() {
        return "var('" + name + "')";
    }
}
