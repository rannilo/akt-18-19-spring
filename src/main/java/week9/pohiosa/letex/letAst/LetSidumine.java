package week9.pohiosa.letex.letAst;

import java.util.Objects;

/**
 * LetAvaldis, mis saadakse mingis teises avaldises ÜHE muutuja sidumisel.
 * Muutuja sidumiseks tuleb anda sellele väärtuseks samuti mingi avaldis.
 * <p>
 * NB! Juba seotud muutuja uuesti sidumine kehtib ainult alamAvaldise piires.
 */
public class LetSidumine extends LetAvaldis {
    private String muutujaNimi;
    private LetAvaldis muutujaSisu;
    private LetAvaldis keha;

    public LetSidumine(String muutujaNimi, LetAvaldis muutujaSisu, LetAvaldis keha) {
        this.muutujaNimi = muutujaNimi;
        this.muutujaSisu = muutujaSisu;
        this.keha = keha;
    }

    public String getMuutujaNimi() {
        return muutujaNimi;
    }

    public LetAvaldis getMuutujaSisu() {
        return muutujaSisu;
    }

    public LetAvaldis getKeha() {
        return keha;
    }

    @Override
    public String toString() {
        return "let(\"" + getMuutujaNimi() + "\"," + getMuutujaSisu() + "," + getKeha() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LetSidumine that = (LetSidumine) o;
        return Objects.equals(muutujaNimi, that.muutujaNimi) &&
                Objects.equals(muutujaSisu, that.muutujaSisu) &&
                Objects.equals(keha, that.keha);
    }

    @Override
    public int hashCode() {

        return Objects.hash(muutujaNimi, muutujaSisu, keha);
    }

    @Override
    public <T> T accept(LetVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
