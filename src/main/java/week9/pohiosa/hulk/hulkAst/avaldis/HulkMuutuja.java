package week9.pohiosa.hulk.hulkAst.avaldis;

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
}
