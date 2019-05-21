package week9.pohiosa.hulk.hulkAst.avaldis;

import week9.pohiosa.hulk.hulkAst.HulkVisitor;

// Esindab hulga nime. Kokkuleppeliselt ladina suurtähed.
public class HulkMuutuja extends HulkAvaldis {
    private Character nimi;

    public HulkMuutuja(Character nimi) {
        this.nimi = nimi;
    }

    public Character getNimi() {
        return nimi;
    }

    @Override
    protected Object getNodeInfo() {
        return nimi;
    }

    @Override
    public String toString() {
        return nimi.toString();
    }

    @Override
    public <T> T accept(HulkVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
