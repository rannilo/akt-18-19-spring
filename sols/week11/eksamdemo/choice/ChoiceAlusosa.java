package week11.eksamdemo.choice;

import week11.eksamdemo.choice.choiceAst.ChoiceVisitor;
import week11.eksamdemo.choice.choiceAst.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ChoiceAlusosa {

    // Väärtusta avaldist otse. Valikuoperaator kasutab juhuarvugeneraatorit,
    // et küsida nextBoolean abil, kas valida vasakpoolse või parempoolse argumendi.
    public static int eval(ChoiceNode expr, Random rnd) {
        return new ChoiceVisitor<Integer>() {
            @Override
            protected Integer visit(ChoiceValue value) {
                return value.getValue();
            }

            @Override
            protected Integer visit(ChoiceAdd add) {
                return visit(add.getLeft()) + visit(add.getRight());
            }

            @Override
            protected Integer visit(ChoiceDecision decision) {
                return rnd.nextBoolean() ?
                        visit(decision.getTrueChoice()) : visit(decision.getFalseChoice());
            }
        }.visit(expr);
    }

    // Nüüd tagastada kõik võimalikud väärtused hulgana.
    public static Set<Integer> allValues(ChoiceNode expr) {
        return new ChoiceVisitor<Set<Integer>>() {
            @Override
            protected Set<Integer> visit(ChoiceValue value) {
                return Collections.singleton(value.getValue());
            }

            @Override
            protected Set<Integer> visit(ChoiceAdd add) {
                Set<Integer> result = new HashSet<>();
                for (Integer x : visit(add.getLeft())) {
                    for (Integer y : visit(add.getRight())) {
                        result.add(x + y);
                    }
                }
                return result;
            }

            @Override
            protected Set<Integer> visit(ChoiceDecision decision) {
                Set<Integer> result = new HashSet<>();
                result.addAll(visit(decision.getFalseChoice()));
                result.addAll(visit(decision.getTrueChoice()));
                return result;
            }
        }.visit(expr);
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
