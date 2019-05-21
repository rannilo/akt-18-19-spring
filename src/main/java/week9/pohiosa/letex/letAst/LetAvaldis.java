package week9.pohiosa.letex.letAst;

/**
 * See on ASTi tippude abstraktne ülemklass.
 * <p>
 * Alamklasside isendite loomiseks saab kasutada vastavaid konstruktoreid.
 */
public abstract class LetAvaldis {
    public static LetAvaldis num(int arv) {
        return new LetArv(arv);
    }

    public static LetAvaldis var(String nimi) {
        return new LetMuutuja(nimi);
    }

    public static LetAvaldis vahe(LetAvaldis vasak, LetAvaldis parem) {
        return new LetVahe(vasak, parem);
    }

    public static LetAvaldis let(String muutujaNimi, LetAvaldis muutujaSisu, LetAvaldis keha) {
        return new LetSidumine(muutujaNimi, muutujaSisu, keha);
    }

    public static LetAvaldis sum(String muutujaNimi, LetAvaldis lo, LetAvaldis hi, LetAvaldis keha) {
        return new LetSumma(muutujaNimi, lo, hi, keha);
    }

    public abstract <T> T accept(LetVisitor<T> visitor);
}
