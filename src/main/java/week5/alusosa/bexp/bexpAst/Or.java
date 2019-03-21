package week5.alusosa.bexp.bexpAst;

public class Or extends BExp {

    public Or(BExp e1, BExp e2) {
        super(e1, e2);
    }

    public BExp getLeft() {
        return getChild(0);
    }

    public BExp getRight() {
        return getChild(1);
    }

    @Override
    public <T> T accept(BExpVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
