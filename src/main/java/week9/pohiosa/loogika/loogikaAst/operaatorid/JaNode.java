package week9.pohiosa.loogika.loogikaAst.operaatorid;

import week9.pohiosa.loogika.loogikaAst.LoogikaNode;

public class JaNode extends BinOpNode {

    public JaNode(LoogikaNode leftChild, LoogikaNode rightChild) {
        super(leftChild, rightChild);
    }

    @Override
    public String getOpName() {
        return "JA";
    }
}
