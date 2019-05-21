package week11.loviosa;

import week9.pohiosa.letex.letAst.LetAvaldis;

import static week9.pohiosa.letex.letAst.LetAvaldis.*;

public class LetAnalyzer {


    public static boolean onVaja(String vajaMuutuja, LetAvaldis avaldis) {
        throw new UnsupportedOperationException();
    }

    public static LetAvaldis optimeeri(LetAvaldis avaldis) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        LetAvaldis avaldis = let("x",num(666),let("y",vahe(var("x"),num(1)),num(3)));
        System.out.println(avaldis);
        System.out.println(optimeeri(avaldis));
    }

    /**
     * Vana hea eval meetod... tuleb avaldist väärtustada!
     */
    public static int eval(LetAvaldis expr) {
        throw new UnsupportedOperationException();
    }
}
