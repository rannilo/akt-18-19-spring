package week9.pohiosa.hulk.hulkAst;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// Iga AST algab selle tipuga. Programm koosneb lausete listist.
public class HulkProgramm extends HulkNode {
    private List<HulkLause> laused;

    public HulkProgramm(List<HulkLause> laused) {
        this.laused = laused;
    }

    public List<HulkLause> getLaused() {
        return laused;
    }

    public void lisaLause(HulkLause lause) {
        this.laused.add(lause);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HulkProgramm that = (HulkProgramm) o;
        return Objects.equals(laused, that.laused);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), laused);
    }

    @Override
    protected Object getNodeInfo() {
        return laused;
    }

    @Override
    public String toString() {
        return laused.stream().map(HulkLause::toString).collect(Collectors.joining("\n"));
    }
}
