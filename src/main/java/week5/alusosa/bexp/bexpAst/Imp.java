package week5.alusosa.bexp.bexpAst;

public class Imp extends BExp {

    public Imp(BExp e, BExp t) {
        super(e, t);
    }

    public BExp getAntedecent() {
        return getChild(0);
    }

    public BExp getConsequent() {
        return getChild(1);
    }

    @Override
    public <T> T accept(BExpVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
