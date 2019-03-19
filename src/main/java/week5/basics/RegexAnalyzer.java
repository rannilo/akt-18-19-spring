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
                return false;
            }

            @Override
            protected Boolean visit(Repetition repetition) {
                return false;
            }

            @Override
            protected Boolean visit(Concatenation concatenation) {
                return false;
            }

            @Override
            protected Boolean visit(Alternation alternation) {
                return false;
            }
        };
        return emptyWordVisitor.visit(regex);
    }

    public static boolean matchesInfManyWords(RegexNode regex) {
        throw new UnsupportedOperationException();
    }

    public static Set<String> getAllWords(RegexNode regex) {
        throw new UnsupportedOperationException();
    }

    private static Set<String> combine(Set<String> s1, Set<String> s2) {
        Set<String> result = new HashSet<>();
        return result;
    }
}
