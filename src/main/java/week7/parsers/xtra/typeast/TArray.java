package week7.parsers.xtra.typeast;

public class TArray extends Type {
    private final Type elem;

    public TArray(Type elem) {
        super("TArray");
        this.add(elem);
        this.elem = elem;
    }

    protected String toSyntax(String rest) {
        return elem.toSyntax("(" + rest + ")" + "[]");
    }

    protected String toEnglish(boolean singular) {
        StringBuilder sb = new StringBuilder();
        if (singular) sb.append("an array of ");
        else sb.append("arrays of ");
        sb.append(elem.toEnglish(false));
        return sb.toString();
    }
}
