package week9.pohiosa.hulk.hulkAst;

import week9.pohiosa.hulk.hulkAst.avaldis.HulkAvaldis;
import week9.pohiosa.hulk.hulkAst.avaldis.HulkLiteraal;
import week9.pohiosa.hulk.hulkAst.avaldis.HulkMuutuja;
import week9.pohiosa.hulk.hulkAst.avaldis.HulkTehe;

import java.util.*;

// See on ASTi tippude abstraktne ülemklass.
// Siin olevate staatiliste meetodite abil saad ehitada ASTi.
public abstract class HulkNode {

    public static HulkProgramm prog(List<HulkLause> laused) {
        return new HulkProgramm(laused);
    }

    public static HulkLause lause(Character nimi, HulkAvaldis avaldis, HulkTingimus tingimus) {
        return new HulkLause(nimi, avaldis, tingimus);
    }

    public static HulkTingimus ting(HulkAvaldis alamHulk, HulkAvaldis ylemHulk) {
        return new HulkTingimus(alamHulk, ylemHulk);
    }

    public static HulkAvaldis var(Character nimi) {
        return new HulkMuutuja(nimi);
    }

    public static HulkAvaldis lit(Character... elemendid) {
        return new HulkLiteraal(new HashSet<>(Arrays.asList(elemendid)));
    }

    public static HulkAvaldis lit(Set<Character> elemendid) {
        return new HulkLiteraal(elemendid);
    }

    public static HulkAvaldis tehe(HulkAvaldis vasak, HulkAvaldis parem, Character op) {
        return new HulkTehe(vasak, parem, op);
    }

    private final List<HulkNode> children;

    public HulkNode(HulkNode... children) {
        this.children = Arrays.asList(children);
    }

    public List<HulkNode> getChildren() {
        return children;
    }

    public HulkNode getChild(int i) {
        return children.get(i);
    }

    // See on meil sisemiselt equals ja hashcode arvutamiseks.
    protected abstract Object getNodeInfo();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        HulkNode that = (HulkNode) obj;
        return Objects.equals(this.getNodeInfo(), that.getNodeInfo()) &&
                Objects.equals(this.getChildren(), that.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNodeInfo(), getChildren());
    }

    public abstract <T> T accept(HulkVisitor<T> visitor);
}
