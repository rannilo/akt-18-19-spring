package week9.pohiosa.hulk.hulkAst;

import week9.pohiosa.hulk.hulkAst.avaldis.HulkLiteraal;
import week9.pohiosa.hulk.hulkAst.avaldis.HulkMuutuja;
import week9.pohiosa.hulk.hulkAst.avaldis.HulkTehe;

public abstract class HulkVisitor<T> {

    public abstract T visit(HulkLiteraal literaal);
    public abstract T visit(HulkMuutuja muutuja);
    public abstract T visit(HulkTehe tehe);
    public abstract T visit(HulkLause lause);
    public abstract T visit(HulkProgramm programm);
    public abstract T visit(HulkTingimus tingimus);

    public T visit(HulkNode node) {
        return node.accept(this);
    }
}
