package week9.pohiosa.loogika.loogikaAst;

public class KuiSiisMuidu extends LoogikaNode {

    public KuiSiisMuidu(LoogikaNode kuiAvaldis, LoogikaNode siisAvaldis, LoogikaNode muiduAvaldis) {
        super(kuiAvaldis, siisAvaldis, muiduAvaldis);
    }

    public KuiSiisMuidu(LoogikaNode kuiAvaldis, LoogikaNode siisAvaldis) {
        super(kuiAvaldis, siisAvaldis, null);
    }

    public LoogikaNode getKuiAvaldis() {
        return getChild(0);
    }

    public LoogikaNode getSiisAvaldis() {
        return getChild(1);
    }

    public LoogikaNode getMuiduAvaldis() {
        return getChild(2);
    }

    @Override
    public String toString() {
        String result = "KUI " + getKuiAvaldis().toString() + " SIIS " + getSiisAvaldis().toString();
        if (getMuiduAvaldis() != null) {
            result += " MUIDU " + getMuiduAvaldis().toString();
        }
        return "(" + result + ")";
    }

    @Override
    protected Object getNodeInfo() {
        return "KUI";
    }

    @Override
    public <T> T accept(LoogikaVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
