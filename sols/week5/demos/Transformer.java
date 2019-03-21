package week5.demos;

import week5.expr.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Transformer {

    // Kõik avaldise arvud panna listi
    public static List<Integer> valueList(ExprNode expr) {
        List<Integer> elems = new LinkedList<>();

        new ExprVisitor.BaseVisitor<Void>() {
            @Override
            public Void visit(Num num) {
                elems.add(num.getValue());
                return null;
            }
        }.visit(expr);

        return elems;
    }

    //Jagamised asendada nende väärtustega...
    public static ExprNode replaceDivs(ExprNode expr) {
        return new ExprVisitor<ExprNode>() {
            @Override
            public ExprNode visit(Num num) {
                // Võime lihtsalt sama tipu tagastada, kuna teda ei saa muuta.
                return num;
            }

            @Override
            protected ExprNode visit(Neg neg) {
                // Siin peab koopia tegema, sest miinusmärgi all teeme teisendusi.
                return new Neg(visit(neg.getExpr()));
            }

            @Override
            public ExprNode visit(Add add) {
                // Jällegi uus tipp teisendatud alampuudega.
                return new Add(visit(add.getLeft()), visit(add.getRight()));
            }

            @Override
            public ExprNode visit(Div div) {
                // Siin lõpuks toimub sisuline teisendus!
                return new Num(div.eval());
            }
        }.visit(expr);
    }
    
        
    // Nüüd kokku koguda arvud niimoodi, et summa jääks sama nagu avaldises..
    public static List<Integer> signedValueList(ExprNode expr) {
        return new ExprVisitor<List<Integer>>() {

            @Override
            public List<Integer> visit(Add add) {
                LinkedList<Integer> list = new LinkedList<>();
                list.addAll(visit(add.getLeft()));
                list.addAll(visit(add.getRight()));
                return list;
            }

            @Override
            public List<Integer> visit(Neg neg) {
                List<Integer> list = visit(neg.getExpr());
                // Kõikide elementide märke tuleb lihtsalt muuta:
                return list.stream().map(i -> -i).collect(Collectors.toList());
            }

            @Override
            public List<Integer> visit(Num num) {
                return Collections.singletonList(num.getValue());
            }

            @Override
            public List<Integer> visit(Div div) {
                throw new UnsupportedOperationException();
            }

        }.visit(expr);
    }
    
    
    // Miinuste elimineerimine:
    public static ExprNode elimNeg(ExprNode expr) {
        return new ExprVisitor<ExprNode>() {
            int sign = 1;

            @Override
            public ExprNode visit(Neg neg) {
                return visit(neg.getExpr(), -sign);
            }

            @Override
            public ExprNode visit(Num num) {
                return new Num(sign * num.getValue());
            }

            @Override
            public ExprNode visit(Div div) {
                return new Div(visit(div.getNumerator()), visit(div.getDenominator(), 1));
            }

            @Override
            public ExprNode visit(Add add) {
                return new Add(visit(add.getLeft()), visit(add.getRight()));
            }

            // A bit of a parameter passing hack here...
            private ExprNode visit(ExprNode node, int newsign) {
                int store = sign;
                sign = newsign;
                ExprNode result = super.visit(node);
                sign = store;
                return result;
            }

        }.visit(expr);
    }
}

