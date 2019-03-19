package week5.demos;

import week5.expr.*;
import week5.regex.*;

public class PrettyPrinter {

    // Ülesanne on ilusasti (see tähendab võimalikult väheste sulgudega) väljastada regulaaravaldised:
    public static String pretty(RegexNode regex) {
        return regex.toString();
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
