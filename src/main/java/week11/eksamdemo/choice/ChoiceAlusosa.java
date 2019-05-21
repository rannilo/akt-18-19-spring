package week11.eksamdemo.choice;

import week11.eksamdemo.choice.choiceAst.ChoiceNode;

import java.util.Random;
import java.util.Set;

public class ChoiceAlusosa {

    // Väärtusta avaldist otse. Valikuoperaator kasutab juhuarvugeneraatorit,
    // et küsida nextBoolean abil, kas valida vasakpoolse või parempoolse argumendi.
    public static int eval(ChoiceNode expr, Random rnd) {
        throw new UnsupportedOperationException();
    }

    // Nüüd tagastada kõik võimalikud väärtused hulgana.
    public static Set<Integer> allValues(ChoiceNode expr) {
        throw new UnsupportedOperationException();
    }


    // Lisada avaldisele täisarv
    public static ChoiceNode addConst(ChoiceNode expr, int n) {
        throw new UnsupportedOperationException();
    }

}
