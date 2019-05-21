package week9.pohiosa.hulk.hulkAst.avaldis;

import week9.pohiosa.hulk.hulkAst.HulkVisitor;

import java.util.Set;

// Esindab elementide hulka.
// Elementideks on Characterid (kokkuleppeliselt väiketähed)
public class HulkLiteraal extends HulkAvaldis {
    private Set<Character> elemendid;

    public HulkLiteraal(Set<Character> elemendid) {
        this.elemendid = elemendid;
    }

    public Set<Character> getElemendid() {
        return elemendid;
    }

    @Override
    protected Object getNodeInfo() {
        return elemendid;
    }

    @Override
    public String toString() {
        return elemendid.toString().replace('[', '{').replace(']', '}');
    }

    @Override
    public <T> T accept(HulkVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
