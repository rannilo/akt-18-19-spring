package week9.pohiosa.letex.letAst;


import java.util.Objects;

/**
 * LetAvaldis, mis seisneb mingi teise avaldise mingite väärtuste omavahelises liitmises.
 * LetSumma seob enda alamavaldises ÜHE muutuja, mille väärtus on igal korral erinev.
 * Tsüklimuutuja algväärtuseks on avaldise lo väärtus, ning seda hakatakse suurendama
 * ühe kaupa, kuni tema väärtus on väiksem kui avaldise hi väärtus.
 * <p>
 * NB! Avaldise hi väärtus on välja arvatud ega saa kunagi tsüklimuutuja väärtuseks.
 */
public class LetSumma extends LetAvaldis {
    private String muutujaNimi;
    private LetAvaldis lo;
    private LetAvaldis hi;
    private LetAvaldis keha;

    public LetSumma(String muutujaNimi, LetAvaldis lo, LetAvaldis hi, LetAvaldis keha) {
        this.muutujaNimi = muutujaNimi;
        this.lo = lo;
        this.hi = hi;
        this.keha = keha;
    }

    public String getMuutujaNimi() {
        return muutujaNimi;
    }

    public LetAvaldis getLo() {
        return lo;
    }

    public LetAvaldis getHi() {
        return hi;
    }

    public LetAvaldis getKeha() {
        return keha;
    }

    @Override
    public String toString() {
        return "sum(\"" + getMuutujaNimi() + "\"," + getLo() + "," + getHi() + "," + getKeha() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LetSumma letSumma = (LetSumma) o;
        return Objects.equals(muutujaNimi, letSumma.muutujaNimi) &&
                Objects.equals(lo, letSumma.lo) &&
                Objects.equals(hi, letSumma.hi) &&
                Objects.equals(keha, letSumma.keha);
    }

    @Override
    public int hashCode() {

        return Objects.hash(muutujaNimi, lo, hi, keha);
    }
}
