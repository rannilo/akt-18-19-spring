package week9.pohiosa.loogika.loogikaAst.aatomid;

import week9.pohiosa.loogika.loogikaAst.LoogikaNode;
import week9.pohiosa.loogika.loogikaAst.LoogikaVisitor;

public class LoogikaLiteral extends LoogikaNode {
    private boolean value;

    public LoogikaLiteral(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }


    @Override
    protected Object getNodeInfo() {
        return value;
    }

    @Override
    public String toString() {
        return value ? "1" : "0";
    }

    @Override
    public <T> T accept(LoogikaVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
