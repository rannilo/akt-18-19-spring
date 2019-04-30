package week9.pohiosa.hulk.hulkAst.avaldis;

// Esindab tehet kahe hulga vahel. Lubatud on:
// '+'  ühend
// '&'  ühisosa
// '-'  vahe
public class HulkTehe extends HulkAvaldis {
    private Character op;

    public HulkTehe(HulkAvaldis vasak, HulkAvaldis parem, Character op) {
        super(vasak, parem);
        if (op == '+' || op == '&' || op == '-')
            this.op = op;
        else throw new IllegalArgumentException(op.toString());
    }

    public HulkAvaldis getVasak() {
        return (HulkAvaldis) getChild(0);
    }

    public HulkAvaldis getParem() {
        return (HulkAvaldis) getChild(1);
    }

    public Character getOp() {
        return op;
    }

    @Override
    protected Object getNodeInfo() {
        return op;
    }

    @Override
    public String toString() {
        return "(" + getVasak().toString() + op.toString() + getParem().toString() + ")";
    }
}
