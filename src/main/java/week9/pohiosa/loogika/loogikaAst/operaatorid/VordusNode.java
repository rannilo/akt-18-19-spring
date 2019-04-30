package week9.pohiosa.loogika.loogikaAst.operaatorid;

import week9.pohiosa.loogika.loogikaAst.LoogikaNode;

public class VordusNode extends BinOpNode {

    public VordusNode(LoogikaNode leftChild, LoogikaNode rightChild) {
        super(leftChild, rightChild);
    }

    public LoogikaNode getLeft() {
        return getChild(0);
    }

    public LoogikaNode getRight() {
        return getChild(1);
    }

    @Override
    public String getOpName() {
        return "=";
    }
}
