package week5.demos;

import week5.expr.*;
import week5.regex.*;

public class PrettyPrinter {

    // Ülesanne on ilusasti (see tähendab võimalikult väheste sulgudega) väljastada regulaaravaldised:
    public static String pretty(RegexNode regex) {
        RegexVisitor<StringBuilder> prettyRegexVisitor = new RegexVisitor<StringBuilder>() {
            StringBuilder sb = new StringBuilder();

            @Override
            protected StringBuilder visit(Letter letter) {
                return sb.append(letter);
            }

            @Override
            protected StringBuilder visit(Epsilon epsilon) {
                return sb.append(epsilon);
            }

            @Override
            protected StringBuilder visit(Alternation alt) {
                visit(alt.getLeft(), priorityOf(alt)).append('|');
                visit(alt.getRight(), priorityOf(alt));
                return sb;
            }

            @Override
            protected StringBuilder visit(Concatenation concat) {
                visit(concat.getLeft(), priorityOf(concat));
                visit(concat.getRight(), priorityOf(concat));
                return sb;
            }

            @Override
            protected StringBuilder visit(Repetition rep) {
                visit(rep.getChild(), priorityOf(rep)+1); // <-- (a*)* ümber lisasulud.
                return sb.append('*');
            }

            private StringBuilder visit(RegexNode node, int contextPrio) {
                boolean paren = priorityOf(node) < contextPrio;
                if (paren) sb.append('(');
                super.visit(node);
                if (paren) sb.append(')');
                return sb;
            }

        };
        return prettyRegexVisitor.visit(regex).toString();
    }

    private static int priorityOf(RegexNode regex) {
        if (regex instanceof Alternation) return 1;
        if (regex instanceof Concatenation) return 2;
        if (regex instanceof Repetition) return 3;
        return 4;
    }

    // Siin on näide aritmeetiliste avaldiste ilutrükki kohta.
    public static String pretty(ExprNode expr) {
        ExprVisitor<String> prettyExprVisitor = new ExprVisitor<String>() {
            @Override
            public String visit(Num num) {
                return Integer.toString(num.getValue());
            }

            @Override
            public String visit(Neg neg) {
                return "-" + visit(neg.getExpr(), priorityOf(neg));
            }

            @Override
            public String visit(Add add) {
                return visit(add.getLeft(), priorityOf(add)) + "+" + visit(add.getRight(), priorityOf(add));
            }

            @Override
            public String visit(Div div) {
                return visit(div.getNumerator(), priorityOf(div)) + "/" +
                        visit(div.getDenominator(), priorityOf(div) + 1); // <-- vasakassotsiatiivsus;
            }

            // Põhiline abimeetod, mis otsustab konteksti põhjal ära, kas sulud on vaja või mitte:
            private String visit(ExprNode expr, int contextPriority) {
                String output = super.visit(expr);
                if (priorityOf(expr) < contextPriority) output = '(' + output + ')';
                return output;
            }

        };
        return prettyExprVisitor.visit(expr);
    }


    private static int priorityOf(ExprNode expr) {
        if (expr instanceof Add) return 1;
        if (expr instanceof Div) return 2;
        if (expr instanceof Neg) return 3;
        return 4;
    }
}
