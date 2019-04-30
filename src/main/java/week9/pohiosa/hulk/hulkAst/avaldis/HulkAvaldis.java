package week9.pohiosa.hulk.hulkAst.avaldis;

import week9.pohiosa.hulk.hulkAst.HulkNode;

// Avaldise abstraktne ülemklass.
// Avaldiseks võivad olla HulkLiteraal, HulkMuutuja ja HulkTehe
public abstract class HulkAvaldis extends HulkNode {
    public HulkAvaldis(HulkNode... children) {
        super(children);
    }
}
