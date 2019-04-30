package week9.pohiosa.loogika.loogikaAst.aatomid;

import week9.pohiosa.loogika.loogikaAst.LoogikaNode;

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

}
