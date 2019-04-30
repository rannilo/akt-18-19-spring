package week9.pohiosa.loogika.loogikaAst.aatomid;

import week9.pohiosa.loogika.loogikaAst.LoogikaNode;

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


}
