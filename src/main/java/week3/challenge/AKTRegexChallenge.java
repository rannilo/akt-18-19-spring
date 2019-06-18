package week3.challenge;

public class AKTRegexChallenge {

    // NB! Epsiloni asemel tuleb kasutada tühjad sulud: ()
    // Aga küsimärgi ja tärni/plussi kasutades ei ole isegi epsilonit siin vaja.


    // Õpiku ülesanne: täisarvuliteraalid, mille väärtus ei ole 42.
    public static final String L1 = "(0*[1-3][0-9])|(0*4[0-1])|(0*4[3-9])|(0*[5-9][0-9])|(0*[1-9][0-9][0-9]+)|(0*[0-9])";

    // Täisarvliteraalid, mille väärtus on rangelt suurem kui 42.
    public static final String L2 = "0*((4[3-9])|([5-9][0-9])|([1-9][0-9][0-9]+))";

    // Tähestiku {0,1} sõnad, milles esinevad täpselt üks '1' ja vähemalt üks '0'.
    public static final String RE1 = "(0+10*)|(0*10+)";

    // Tähestiku {a,b} sõnad, mis sisaldavad paarisarv a-sid ja paarisarv b-sid.
    public static final String RE2 = "(aa|ab(bb)*ba|(b|ab(bb)*a)(a(bb)*a)*(b|a(bb)*ba))*";

    // Tähestiku {0,1} Sõnad, mis ei sisalda 101.
    public static final String RE3 = "(0|11*00)*(()|11*|11*0)";

    // Kolmega jaguvad binaararvud.
    public static final String RE4 = "(0|11|10(1|00)*01)+";

}

