package eksam2.ast;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BologImp extends BologNode {

    private List<BologNode> assumptions;
    private BologNode conclusion;

    public List<BologNode> getAssumptions() {
        return assumptions;
    }

    public BologNode getConclusion() {
        return conclusion;
    }

    public BologImp(BologNode conclusion, List<BologNode> assumptions) {
        this.assumptions = assumptions;
        this.conclusion = conclusion;
    }

    public BologImp(BologNode conclusion, BologNode... assumptions) {
        this(conclusion, Arrays.asList(assumptions));
    }

    @Override
    public String toString() {
        String assumeString = assumptions.stream().map(n -> ", " + n).collect(Collectors.joining());
        return "imp(" + conclusion +  assumeString + ")";
    }

    @Override
    public <T> T accept(BologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
