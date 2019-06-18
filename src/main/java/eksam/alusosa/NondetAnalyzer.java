package eksam.alusosa;

import eksam.alusosa.nondetAst.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

public class NondetAnalyzer {

    public static Set<Integer> getAllLeafsNotUnder(NdExpr expr, String question) {
        Set<NdExpr> choiceSubtrees = getLargestChoiceSubtreesWithQuestion(expr, question); //kogun kõik lehed
        Set<Integer> allLeafs = getAllLeafs(expr);

        Set<Integer> choiceLeafs = new HashSet<>(); //kogun kokku kõik küsimusega alampuude lehed
        for(NdExpr choiceExpr: choiceSubtrees){
            choiceLeafs.addAll(getAllLeafs(choiceExpr));
        }

        allLeafs.removeAll(choiceLeafs); //eemaldan küsimusega alampude lehed
        return allLeafs;
    }

    private static Set<NdExpr> getLargestChoiceSubtreesWithQuestion(NdExpr expr, String question){
        NdVisitor<Set<NdExpr>> visitor = new NdVisitor<Set<NdExpr>>() {
            @Override
            protected Set<NdExpr> visit(NdNum num) {
                return new HashSet<>();
            }

            @Override
            protected Set<NdExpr> visit(NdInc inc) {
                return new HashSet<>();
            }

            @Override
            protected Set<NdExpr> visit(NdMul mul) {
                Set<NdExpr> left = visit(mul.getLeft());
                Set<NdExpr> right = visit(mul.getRight());
                Set<NdExpr> aggregateSet = new HashSet<>();
                aggregateSet.addAll(left);
                aggregateSet.addAll(right);
                return aggregate(visit(mul.getRight()), visit(mul.getLeft()));
            }

            @Override
            protected Set<NdExpr> visit(NdChoice choice) {
                if(choice.getQuestion().equals(question)) return Collections.singleton(choice);
                else return aggregate(visit(choice.getFalseChoice()), visit(choice.getTrueChoice()));
            }
            private Set<NdExpr> aggregate(Set<NdExpr> s1, Set<NdExpr> s2){
                Set<NdExpr> aggregateSet = new HashSet<>();
                aggregateSet.addAll(s1);
                aggregateSet.addAll(s2);
                return aggregateSet;
            }
        };
        return expr.accept(visitor);
    }

    private static Set<Integer> getAllLeafs(NdExpr expr){
        NdVisitor<Set<Integer>> visitor = new NdVisitor<Set<Integer>>() {
            @Override
            protected Set<Integer> visit(NdNum num) {
                return Collections.singleton(num.getValue());
            }

            @Override
            protected Set<Integer> visit(NdInc inc) {
                return visit(inc.getExpr());
            }

            @Override
            protected Set<Integer> visit(NdMul mul) {
                return aggregate(visit(mul.getLeft()), visit(mul.getRight()));
            }

            @Override
            protected Set<Integer> visit(NdChoice choice) {
                return aggregate(visit(choice.getTrueChoice()), visit(choice.getFalseChoice()));
            }
            private Set<Integer> aggregate(Set<Integer> s1, Set<Integer> s2){
                Set<Integer> aggregateSet = new HashSet<>();
                aggregateSet.addAll(s1);
                aggregateSet.addAll(s2);
                return aggregateSet;
            }
        };
        return expr.accept(visitor);
    }


    public static int eval(NdExpr expr, Predicate<String> oracle) {
        NdVisitor<Integer> visitor = new NdVisitor<Integer>() {
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
                if(oracle.test(choice.getQuestion())) return visit(choice.getTrueChoice());
                else return visit(choice.getFalseChoice());
            }
        };
        return expr.accept(visitor);
    }


    public static boolean isDeterministic(NdExpr expr) {
        Set<Integer> allPossibleAnswers = getAllPossibleAnswers(expr);
        return allPossibleAnswers.size() == 1;
    }

    private static Set<Integer> getAllPossibleAnswers(NdExpr expr){
        NdVisitor<Set<Integer>> visitor = new NdVisitor<Set<Integer>>() {
            @Override
            protected Set<Integer> visit(NdNum num) {
                return Collections.singleton(num.getValue());
            }

            @Override
            protected Set<Integer> visit(NdInc inc) {
                Set<Integer> set = visit(inc.getExpr());
                Set<Integer> set1 = new HashSet<>();
                for(Integer el: set){
                    set1.add(el+1);
                }
                return set1;
            }

            @Override
            protected Set<Integer> visit(NdMul mul) {
                Set<Integer> left = visit(mul.getLeft());
                Set<Integer> right = visit(mul.getRight());
                Set<Integer> set1 = new HashSet<>();
                for(Integer l: left){
                    for(Integer r: right){
                        set1.add(l*r);
                    }
                }
                return set1;
            }

            @Override
            protected Set<Integer> visit(NdChoice choice) {
                Set<Integer> aggregate = new HashSet<>();
                aggregate.addAll(visit(choice.getTrueChoice()));
                aggregate.addAll(visit(choice.getFalseChoice()));
                return aggregate;
            }
        };
        return expr.accept(visitor);
    }

}
