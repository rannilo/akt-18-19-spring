package week5.alusosa.bexp.bexpAst;

public class Not extends BExp {

    public Not(BExp e) {
        super(e);
    }

    public BExp getExp() {
        return getChild(0);
    }

    @Override
    public <T> T accept(BExpVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
