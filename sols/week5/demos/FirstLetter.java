package week5.demos;

import week5.regex.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FirstLetter {
    public static Set<Character> getFirst(String regex) {
        RegexNode node = RegexParser.parse(regex);
        return new GetFirstVisitor().visit(node).chars;
    }

    private static class GetFirstVisitor extends RegexVisitor<ResultType> {
        @Override
        protected ResultType visit(Alternation alternation) {
            ResultType leftResult = visit(alternation.getLeft());
            ResultType rightResult = visit(alternation.getRight());
            Set<Character> chars = new HashSet<>(leftResult.chars);
            chars.addAll(rightResult.chars);
            return new ResultType(chars, leftResult.emptiness || rightResult.emptiness);
        }

        @Override
        protected ResultType visit(Concatenation concatenation) {
            ResultType leftResult = visit(concatenation.getLeft());
            ResultType rightResult = visit(concatenation.getRight());
            Set<Character> chars = new HashSet<>(leftResult.chars);
            // Siin on see oluline koht, kus kontrollime tühjust:
            if (leftResult.emptiness) chars.addAll(rightResult.chars);
            return new ResultType(chars, leftResult.emptiness && rightResult.emptiness);
        }

        @Override
        protected ResultType visit(Epsilon epsilon) {
            return new ResultType(Collections.emptySet(), true);
        }

        @Override
        protected ResultType visit(Letter letter) {
            return new ResultType(Collections.singleton(letter.getSymbol()), false);
        }

        @Override
        protected ResultType visit(Repetition repetition) {
            ResultType childResult = visit(repetition.getChild());
            return new ResultType(childResult.chars, true);
        }
    }

    private static class ResultType {
        Set<Character> chars;
        boolean emptiness;

        public ResultType(Set<Character> chars, boolean emptiness) {
            this.chars = chars;
            this.emptiness = emptiness;
        }
    }
}
