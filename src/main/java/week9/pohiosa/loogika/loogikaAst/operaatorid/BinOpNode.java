package week9.pohiosa.loogika.loogikaAst.operaatorid;

import week9.pohiosa.loogika.loogikaAst.LoogikaNode;

public abstract class BinOpNode extends LoogikaNode {

    public BinOpNode(LoogikaNode left, LoogikaNode right) {
        super(left, right);
    }

    public LoogikaNode getLeftChild() {
        return getChild(0);
    }

    public LoogikaNode getRightChild() {
        return getChild(1);
    }

    public abstract String getOpName();

    @Override
    protected Object getNodeInfo() {
        return getOpName();
    }

    @Override
    public String toString() {
        String left = getLeftChild().toString();
        String right = getRightChild().toString();
        String op = getOpName();
        return "(" + left + " " + op + " " + right + ")";
    }
}
