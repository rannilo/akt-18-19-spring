package week9.pohiosa.hulk.hulkAst;

import week9.pohiosa.hulk.hulkAst.avaldis.HulkAvaldis;

// Tingimus esindab alamhulgaks olemist.
// Koosneb kahest avaldisest: alamHulk ja ylemHulk. On tõene siis, kui iga alamHulga element kuulub ka ylemHulka.
public class HulkTingimus extends HulkNode {

    public HulkTingimus(HulkAvaldis alamHulk, HulkAvaldis ylemHulk) {
        super(alamHulk, ylemHulk);
    }

    public HulkAvaldis getAlamHulk() {
        return (HulkAvaldis) getChild(0);
    }

    public HulkAvaldis getYlemHulk() {
        return (HulkAvaldis) getChild(1);
    }

    @Override
    protected Object getNodeInfo() {
        return "TINGIMUS";
    }

    @Override
    public String toString() {
        return getAlamHulk().toString() + " subset " + getYlemHulk().toString();
    }
}
