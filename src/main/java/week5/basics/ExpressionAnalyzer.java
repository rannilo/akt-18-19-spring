package week5.basics;

import week5.expr.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ExpressionAnalyzer {

    // Mõnikord me ei taha muuta AST klasse:
    // Siin on ainult üks rida puudu...
    public static int eval(ExprNode expr) {
        if (expr instanceof Num) {
            return ((Num) expr).getValue();
        } else if (expr instanceof Add) {
            Add add = ((Add) expr);
            return eval(add.getLeft()) + eval(add.getRight());
        } else if (expr instanceof Div) {
            Div div = (Div) expr;
            return 0;
        } else {
            assert expr instanceof Neg;
            return -(eval(((Neg) expr).getExpr()));
        }
    }

    // Proovime nüüd selle sama asja visitor abil teha.
    public static int evalWithVisitor(ExprNode expr) {
        // Kirjutame siia "null" asemel "new ExprVisitor<Integer>()" ja siis IDE abil genereeri meetodid.
        ExprVisitor<Integer> visitor = null;
        return expr.accept(visitor);
    }


    // Proovi nüüd ise teha natuke lihtsam ülesanne: koguda kõik tipus esinevad arvud.
    public static Set<Integer> getAllValues(ExprNode expr) {
        ExprVisitor<Set<Integer>> visitor = null;
        return expr.accept(visitor);
    }


    // Proovi nüüd sama kood ilma kordusteta ümber kirjutada kasutades BaseVisitorit.
    public static Set<Integer> getAllValuesBaseVisitor(ExprNode expr) {

        ExprVisitor<Set<Integer>> visitor = new ExprVisitor.BaseVisitor<Set<Integer>>() {
        };

        return expr.accept(visitor);
    }


    // Võime muidugi ka ainult tipudes tegutseda ja elemente lihtsalt hulka kokku koguda.
    // NB! Kasutame siin siseklasse ja kui tahta näiteks loendada tipude arvu, siis Java kurdab,
    // et väline muutuja ei ole "final". Võid proovida "set" asendada reaga "int count = 0" ja elemendi
    // lisamise asemel teha "count++". IntelliJ oskab seda ise asendada ühe-elemendilise jadaga.
    public static Set<Integer> getAllValuesWithBaseVisitorImperative(ExprNode expr) {

        Set<Integer> set = new HashSet<>();

        ExprVisitor<Void> visitor = new ExprVisitor.BaseVisitor<Void>() {
            @Override
            protected Void visit(Num num) {
                set.add(num.getValue());
                return null; // <-- Geneerikute Void tüüp nõuab tagastust :(
            }
        };

        expr.accept(visitor);
        return set;
    }

}
