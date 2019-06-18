package week11.eksamdemo.choice;

import week11.eksamdemo.choice.choiceAst.ChoiceAdd;
import week11.eksamdemo.choice.choiceAst.ChoiceDecision;
import week11.eksamdemo.choice.choiceAst.ChoiceNode;
import week11.eksamdemo.choice.choiceAst.ChoiceValue;
import week11.eksamdemo.choice.choiceAst.ChoiceVisitor;


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
        return new ChoiceVisitor<ChoiceNode>() {
            @Override
            protected ChoiceNode visit(ChoiceValue value) {
                return ChoiceNode.val(value.getValue() + n);
            }

            @Override
            protected ChoiceNode visit(ChoiceAdd add) {
                return ChoiceNode.add(visit(add.getLeft()), visit(add.getRight()));
            }

            @Override
            protected ChoiceNode visit(ChoiceDecision decision) {
                return ChoiceNode.choice(
                        visit(decision.getTrueChoice()),
                        visit(decision.getFalseChoice()));
            }
        }.visit(expr);
    }

}
