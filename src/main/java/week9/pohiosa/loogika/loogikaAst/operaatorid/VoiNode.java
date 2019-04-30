package week9.pohiosa.loogika.loogikaAst.operaatorid;

import week9.pohiosa.loogika.loogikaAst.LoogikaNode;

public class VoiNode extends BinOpNode {
    public VoiNode(LoogikaNode leftChild, LoogikaNode rightChild) {
        super(leftChild, rightChild);
    }

    @Override
    public String getOpName() {
        return "VOI";
    }
}
