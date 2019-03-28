package eksam.alusosa;

import eksam.alusosa.nondetAst.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NondetAnalyzer {

    public static Set<Integer> getAllLeafsNotUnder(NdExpr expr, String question) {
        Set<Integer> result = new HashSet<>();
        new NdVisitor<Void>() {
            @Override
            protected Void visit(NdNum num) {
                result.add(num.getValue());
                return null;
            }

            @Override
            protected Void visit(NdInc inc) {
                return visit(inc.getExpr());
            }

            @Override
            protected Void visit(NdMul mul) {
                visit(mul.getLeft());
                visit(mul.getRight());
                return null;
            }

            @Override
            protected Void visit(NdChoice mychoice) {
                if (!mychoice.getQuestion().equals(question)) {
                    visit(mychoice.getFalseChoice());
                    visit(mychoice.getTrueChoice());
                }
                return null;
            }
        }.visit(expr);
        return result;
    }



    public static int eval(NdExpr expr, Predicate<String> oracle) {
        return new NdVisitor<Integer>() {
            @Override
            protected Integer visit(NdNum num) {
                return num.getValue();
            }

            @Override
            protected Integer visit(NdInc inc) {
                return visit(inc.getExpr()) + 1;
            }

            @Override
            protected Integer visit(NdMul mul) {
                return visit(mul.getLeft()) * visit(mul.getRight());
            }

            @Override
            protected Integer visit(NdChoice choice) {
                return oracle.test(choice.getQuestion()) ? visit(choice.getTrueChoice()) : visit(choice.getFalseChoice());
            }
        }.visit(expr);
    }


    public static boolean isDeterministic(NdExpr expr) {
        // Kõige lihtsam on arvutada kõik võimalike väärtuste hulga.
        // Seda saab muidugi ka for-tsüklitega teha (nagu näiteks RegexAnalyzer.combine),
        // aga ma siin kasutan Java stream'id, et oleks näha paralleel järgmise lahendusvariandiga.
        return new NdVisitor<Set<Integer>>() {
            @Override
            protected Set<Integer> visit(NdNum num) {
                return Collections.singleton(num.getValue());
            }

            @Override
            protected Set<Integer> visit(NdInc inc) {
                return visit(inc.getExpr()).stream().map(x -> x + 1).collect(Collectors.toSet());
            }

            @Override
            protected Set<Integer> visit(NdMul mul) {
                Set<Integer> left = visit(mul.getLeft());
                Set<Integer> right = visit(mul.getRight());
                return left.stream().flatMap(x -> right.stream().map(y -> x * y)).collect(Collectors.toSet());
            }

            @Override
            protected Set<Integer> visit(NdChoice choice) {
                Set<Integer> left = visit(choice.getTrueChoice());
                Set<Integer> right = visit(choice.getFalseChoice());
                return Stream.concat(left.stream(), right.stream()).collect(Collectors.toSet());
            }
        }.visit(expr).size() == 1;
    }

    // Vesalile meeldib see lahendus, kuna kasutab minimaalset lisainfot,
    // aga suureks miinuseks on see, et peab algebralisi võrdusi ise käsitlema!
    public static boolean isDeterministicOptional(NdExpr expr) {
        return new NdVisitor<Optional<Integer>>() {
            @Override
            protected Optional<Integer> visit(NdNum num) {
                return Optional.of(num.getValue());
            }

            @Override
            protected Optional<Integer> visit(NdInc inc) {
                return visit(inc.getExpr()).map(x -> x + 1);
            }

            @Override
            protected Optional<Integer> visit(NdMul mul) {
                Optional<Integer> left = visit(mul.getLeft());
                Optional<Integer> right = visit(mul.getRight());
                // kontrollime kõigepealt 0 * x ja nondet * 0
                if (left.orElse(right.orElse(1)).equals(0)) return Optional.of(0);
                return left.flatMap(x -> right.map(y -> x * y));
            }

            @Override
            protected Optional<Integer> visit(NdChoice choice) {
                return visit(choice.getTrueChoice()).flatMap(x -> visit(choice.getFalseChoice()).filter(x::equals));
            }
        }.visit(expr).isPresent();
    }

}
