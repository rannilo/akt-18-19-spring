package week5.basics;

import week5.regex.*;

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

    public static boolean matchesInfManyWords(RegexNode regex) {
        RegexVisitor<Boolean> manyWordsVisitor = new RegexVisitor<Boolean>() {
            @Override
            protected Boolean visit(Alternation alternation) {
                return visit(alternation.getLeft()) || visit(alternation.getRight());
            }

            @Override
            protected Boolean visit(Concatenation concatenation) {
                return visit(concatenation.getLeft()) || visit(concatenation.getRight());
            }

            @Override
            protected Boolean visit(Epsilon epsilon) {
                return false;
            }

            @Override
            protected Boolean visit(Letter letter) {
                return false;
            }

            @Override
            protected Boolean visit(Repetition repetition) {
                return !matchesOnlyEmptyWord(repetition);
            }
        };
        return manyWordsVisitor.visit(regex);
    }

    //See on eelmise jaoks vajalik abifunktsioon.
    private static boolean matchesOnlyEmptyWord(Repetition regex) {
        RegexVisitor<Boolean> onlyEmptyWordVisitor = new RegexVisitor<Boolean>() {
            @Override
            protected Boolean visit(Alternation alternation) {
                return visit(alternation.getLeft()) && visit(alternation.getRight());
            }

            @Override
            protected Boolean visit(Concatenation concatenation) {
                return visit(concatenation.getLeft()) && visit(concatenation.getRight());
            }

            @Override
            protected Boolean visit(Epsilon epsilon) {
                return true;
            }

            @Override
            protected Boolean visit(Letter letter) {
                return false;
            }

            @Override
            protected Boolean visit(Repetition repetition) {
                return visit(repetition.getChild());
            }
        };
        return onlyEmptyWordVisitor.visit(regex);
    }

    public static Set<String> getAllWords(RegexNode regex) {
        RegexVisitor<Set<String>> allWordsVisitor = new RegexVisitor<Set<String>>() {
            @Override
            protected Set<String> visit(Alternation alternation) {
                Set<String> ret = new HashSet<>(visit(alternation.getLeft()));
                ret.addAll(visit(alternation.getRight()));
                return ret;
            }

            @Override
            protected Set<String> visit(Concatenation concatenation) {
                return combine(visit(concatenation.getLeft()), visit(concatenation.getRight()));
            }

            @Override
            protected Set<String> visit(Epsilon epsilon) {
                return Collections.singleton("");
            }

            @Override
            protected Set<String> visit(Letter letter) {
                return Collections.singleton(Character.toString(letter.getSymbol()));
            }

            @Override
            protected Set<String> visit(Repetition repetition) {
                Set<String> childWords = visit(repetition.getChild());
                if (childWords.equals(Collections.singleton("")))
                    return childWords;
                else
                    throw new RuntimeException("Infinite language");
            }
        };
        return allWordsVisitor.visit(regex);
    }

    private static Set<String> combine(Set<String> s1, Set<String> s2) {
        Set<String> result = new HashSet<>();
        for (String w1 : s1) {
            for (String w2 : s2) {
                result.add(w1 + w2);
            }
        }
        return result;
    }
}
