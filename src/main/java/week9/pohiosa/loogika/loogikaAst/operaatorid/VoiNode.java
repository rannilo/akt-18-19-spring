package week9.pohiosa.loogika.loogikaAst.operaatorid;

import week9.pohiosa.loogika.loogikaAst.LoogikaNode;
import week9.pohiosa.loogika.loogikaAst.LoogikaVisitor;

public class VoiNode extends BinOpNode {
    public VoiNode(LoogikaNode leftChild, LoogikaNode rightChild) {
        super(leftChild, rightChild);
    }

    @Override
    public String getOpName() {
        return "VOI";
    }

    @Override
    public <T> T accept(LoogikaVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
