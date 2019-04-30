package week9.pohiosa.hulk.hulkAst;

import week9.pohiosa.hulk.hulkAst.avaldis.HulkAvaldis;

// Lause esindab hulgale mingi väärtuse omistamist.
// Koosneb hulga nimest (Character), avaldisest ning tingimusest. Tingimus võib olla null.
// Lauset ei täideta kui tingimus on väär.
public class HulkLause extends HulkNode {

    private Character nimi;

    public HulkLause(Character nimi, HulkAvaldis avaldis, HulkTingimus tingimus) {
        super(avaldis, tingimus);
        this.nimi = nimi;
    }

    public Character getNimi() {
        return nimi;
    }

    public HulkAvaldis getAvaldis() {
        return (HulkAvaldis) getChild(0);
    }

    public HulkTingimus getTingimus() {
        return (HulkTingimus) getChild(1);
    }

    @Override
    protected Object getNodeInfo() {
        return nimi;
    }

    @Override
    public String toString() {
        String result = nimi.toString() + " := " + getAvaldis().toString();
        if (getTingimus() != null)
            result += " | " + getTingimus().toString();
        return result;
    }
}
