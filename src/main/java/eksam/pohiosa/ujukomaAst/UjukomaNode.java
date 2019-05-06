package eksam.pohiosa.ujukomaAst;

/**
 * See on ASTi tippude abstraktne ülemklass.
 * <p>
 * Alamklasside isendite loomiseks saab kasutada vastavaid konstruktoreid.
 */
public abstract class UjukomaNode {

    public static UjukomaNode lit(int i) { return new UjukomaLitInt(i); }
    public static UjukomaNode lit(double d) { return new UjukomaLitDouble(d); }
    public static UjukomaNode var(char c) {
        return new UjukomaVar(c);
    }
    public static UjukomaNode op(char sym, UjukomaNode left, UjukomaNode right) {
        return new UjukomaBinop(sym, left, right);
    }
}