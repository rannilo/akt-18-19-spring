package eksam1;

import eksam1.ast.*;

import java.util.*;

import static eksam1.CondAlusosa.Type.*;

public class CondAlusosa {


    // Deklareeritud muutujad, mis programmi avaldises kuskil ei esine.
    public static Set<String> unusedVars(CondNode prog) {
        throw new UnsupportedOperationException();
    }

    public static Object eval(CondNode prog, Map<String, Object> env) {
        throw new UnsupportedOperationException();
    }



    public enum Type { TInt, TBool }

    public static Type typecheck(CondNode prog) {
        throw new UnsupportedOperationException();
    }

}
