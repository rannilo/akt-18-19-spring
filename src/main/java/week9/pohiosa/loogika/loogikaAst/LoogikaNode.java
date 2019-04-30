package week9.pohiosa.loogika.loogikaAst;

import week9.pohiosa.loogika.loogikaAst.aatomid.LoogikaLiteral;
import week9.pohiosa.loogika.loogikaAst.aatomid.LoogikaMuutuja;
import week9.pohiosa.loogika.loogikaAst.operaatorid.JaNode;
import week9.pohiosa.loogika.loogikaAst.operaatorid.VoiNode;
import week9.pohiosa.loogika.loogikaAst.operaatorid.VordusNode;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class LoogikaNode {

    public static LoogikaNode var(String nimi) {
        return new LoogikaMuutuja(nimi);
    }

    public static LoogikaNode lit(boolean value) {
        return new LoogikaLiteral(value);
    }

    public static LoogikaNode ja(LoogikaNode left, LoogikaNode right) {
        return new JaNode(left, right);
    }

    public static LoogikaNode voi(LoogikaNode left, LoogikaNode right) {
        return new VoiNode(left, right);
    }

    public static LoogikaNode vordus(LoogikaNode left, LoogikaNode right) {
        return new VordusNode(left, right);
    }

    public static LoogikaNode kuiSiis(LoogikaNode kui, LoogikaNode siis, LoogikaNode muidu) {
        return new KuiSiisMuidu(kui, siis, muidu);
    }


    private final List<LoogikaNode> children;

    public LoogikaNode(LoogikaNode... children) {
        this.children = Arrays.asList(children);
    }

    public List<LoogikaNode> getChildren() {
        return children;
    }

    public LoogikaNode getChild(int i) {
        return children.get(i);
    }

    // See on meil sisemiselt equals ja hashcode arvutamiseks.
    protected abstract Object getNodeInfo();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        LoogikaNode that = (LoogikaNode) obj;
        return Objects.equals(this.getNodeInfo(), that.getNodeInfo()) &&
                Objects.equals(this.getChildren(), that.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNodeInfo(), getChildren());
    }

}
