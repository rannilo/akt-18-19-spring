package eksam.alusosa.nondetAst;

public class NdChoice extends NdExpr {

    private final String question;
    private final NdExpr trueChoice;
    private final NdExpr falseChoice;

    public NdChoice(String question, NdExpr trueChoice, NdExpr falseChoice) {
        this.question = question;
        this.trueChoice = trueChoice;
        this.falseChoice = falseChoice;
    }

    public String getQuestion() {
        return question;
    }
    public NdExpr getTrueChoice() {
        return trueChoice;
    }
    public NdExpr getFalseChoice() {
        return falseChoice;
    }


    @Override
    public <T> T accept(NdVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
