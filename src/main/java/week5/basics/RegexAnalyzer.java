package week5.basics;

import week5.regex.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RegexAnalyzer {


    public static boolean matchesEmptyWord(RegexNode regex) {
        RegexVisitor<Boolean> emptyWordVisitor = new RegexVisitor<Boolean>() {
            @Override
            protected Boolean visit(Letter letter) {
                return false;
            }

            @Override
            protected Boolean visit(Epsilon epsilon) {
                return true;
            }

            @Override
            protected Boolean visit(Repetition repetition) {
                return true;
            }

            @Override
            protected Boolean visit(Concatenation concatenation) {
                return visit(concatenation.getLeft()) && visit(concatenation.getRight());
            }

            @Override
            protected Boolean visit(Alternation alternation) {
                return visit(alternation.getLeft()) || visit(alternation.getRight());
            }
        };
        return emptyWordVisitor.visit(regex);
    }

    /**
     *
     * @param regex
     * @return true if infinitely many words match the regex
     */
    public static boolean matchesInfManyWords(RegexNode regex) {
        RegexVisitor<Boolean> visitor = new RegexVisitor<Boolean>() {
            @Override
            protected Boolean visit(Letter letter) {
                return false;
            }

            @Override
            protected Boolean visit(Epsilon epsilon) {
                return false;
            }

            @Override
            protected Boolean visit(Repetition repetition) {
                return !matchesOnlyEpsilon(repetition.getChild());
            }

            @Override
            protected Boolean visit(Concatenation concatenation) {
                return visit(concatenation.getLeft()) || visit(concatenation.getRight());
            }

            @Override
            protected Boolean visit(Alternation alternation) {
                return visit(alternation.getLeft()) || visit(alternation.getRight());
            }
        };
        return ((RegexVisitor<Boolean>) visitor).visit(regex);
    }

    /**
     *
     * @param regex
     * @return true only if contains only epsilon-type leafs
     */
    private static boolean matchesOnlyEpsilon(RegexNode regex){
        RegexVisitor<Boolean> epsilonVisitor = new RegexVisitor<Boolean>() {
            @Override
            protected Boolean visit(Letter letter) {
                return false;
            }

            @Override
            protected Boolean visit(Epsilon epsilon) {
                return true;
            }

            @Override
            protected Boolean visit(Repetition repetition) {
                return visit(repetition.getChild());
            }

            @Override
            protected Boolean visit(Concatenation concatenation) {
                return visit(concatenation.getRight()) && visit(concatenation.getLeft());
            }

            @Override
            protected Boolean visit(Alternation alternation) {
                return visit(alternation.getRight()) && visit(alternation.getLeft());
            }
        };
        return ((RegexVisitor<Boolean>) epsilonVisitor).visit(regex);
    }

    public static Set<String> getAllWords(RegexNode regex) {
        if(matchesInfManyWords(regex)) throw new RuntimeException("Regex supports infinitely many words");
        RegexVisitor<Set<String>> visitor = new RegexVisitor<Set<String>>() {
            @Override
            protected Set<String> visit(Letter letter) {
                return Collections.singleton(Character.toString(letter.getSymbol()));
            }

            @Override
            protected Set<String> visit(Epsilon epsilon) {
                return Collections.singleton("");
            }

            @Override
            protected Set<String> visit(Repetition repetition) {
                return visit(repetition.getChild());
            }

            @Override
            protected Set<String> visit(Concatenation concatenation) {
                Set<String> leftSet = visit(concatenation.getLeft());
                Set<String> rightSet = visit(concatenation.getRight());
                Set<String> hulk = new HashSet<>();
                for (String w1: leftSet){
                    for (String w2: rightSet){
                        hulk.add(w1 + w2);
                    }
                }
                return hulk;
            }

            @Override
            protected Set<String> visit(Alternation alternation) {
                Set<String> hulk = new HashSet<>();
                hulk.addAll(visit(alternation.getLeft()));
                hulk.addAll(visit(alternation.getRight()));
                return hulk;
            }
        };
        return ((RegexVisitor<Set<String>>) visitor).visit(regex);
    }

    private static Set<String> combine(Set<String> s1, Set<String> s2) {
        Set<String> result = new HashSet<>();
        for (String w1:s1){
            for (String w2:s2){
                result.add(w1+w2);
            }
        }
        return result;
    }
}
