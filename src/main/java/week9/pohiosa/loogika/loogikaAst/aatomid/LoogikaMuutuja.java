package week9.pohiosa.loogika.loogikaAst.aatomid;

import week9.pohiosa.loogika.loogikaAst.LoogikaNode;
import week9.pohiosa.loogika.loogikaAst.LoogikaVisitor;

public class LoogikaMuutuja extends LoogikaNode {
    private String nimi;

    public LoogikaMuutuja(String nimi) {
        this.nimi = nimi;
    }

    public String getNimi() {
        return nimi;
    }

    @Override
    protected Object getNodeInfo() {
        return nimi;
    }

    @Override
    public String toString() {
        return nimi;
    }

    @Override
    public <T> T accept(LoogikaVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
