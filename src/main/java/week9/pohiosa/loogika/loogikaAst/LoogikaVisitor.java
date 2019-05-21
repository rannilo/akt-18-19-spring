package week9.pohiosa.loogika.loogikaAst;

import week9.pohiosa.loogika.loogikaAst.aatomid.LoogikaLiteral;
import week9.pohiosa.loogika.loogikaAst.aatomid.LoogikaMuutuja;
import week9.pohiosa.loogika.loogikaAst.operaatorid.JaNode;
import week9.pohiosa.loogika.loogikaAst.operaatorid.VoiNode;
import week9.pohiosa.loogika.loogikaAst.operaatorid.VordusNode;

public abstract class LoogikaVisitor<T> {

    public abstract T visit(LoogikaLiteral literal);
    public abstract T visit(LoogikaMuutuja muutuja);
    public abstract T visit(JaNode ja);
    public abstract T visit(VoiNode voi);
    public abstract T visit(VordusNode vordus);
    public abstract T visit(KuiSiisMuidu kuiSiisMuidu);

    public T visit(LoogikaNode node) {
        return node.accept(this);
    }
}
