package week5.alusosa.bexp;

import week5.alusosa.bexp.bexpAst.*;

import java.util.Set;

public class BExpAnalyzer {

    public static Stats analyze(BExp exp) {
        Stats stats = new Stats();

        new BExpVisitor<Void>(){
            @Override
            public Void visit(Var node) {
                stats.addVar(node);
                return null;
            }

            @Override
            public Void visit(Imp node) {
                stats.foundImp();
                visit(node.getAntedecent());
                visit(node.getConsequent());
                return null;
            }

            @Override
            protected Void visit(Or node) {
                visit(node.getLeft());
                visit(node.getRight());
                return null;
            }

            @Override
            protected Void visit(Not node) {
                visit(node.getExp());
                return null;
            }
        }.visit(exp);

        return stats;
    }


    public static boolean eval(BExp exp, Set<Character> tv) {
        return new BExpVisitor<Boolean>() {
            @Override
            protected Boolean visit(Imp node) {
                return !visit(node.getAntedecent()) || visit(node.getConsequent());
            }

            @Override
            protected Boolean visit(Or node) {
                return visit(node.getLeft()) || visit(node.getRight());
            }

            @Override
            protected Boolean visit(Not node) {
                return !visit(node.getExp());
            }

            @Override
            protected Boolean visit(Var node) {
                return tv.contains(node.getName());
            }
        }.visit(exp);
    }

    public static BExp transform(BExp exp) {
        return new BExpVisitor<BExp>() {
            @Override
            protected BExp visit(Imp node) {
                return new Or(
                        new Not(visit(node.getAntedecent())),
                        visit(node.getConsequent()));
            }

            @Override
            protected BExp visit(Or node) {
                return new Or(visit(node.getLeft()), visit(node.getRight()));
            }

            @Override
            protected BExp visit(Not node) {
                return new Not(visit(node.getExp()));
            }

            @Override
            protected BExp visit(Var node) {
                return node;
            }
        }.visit(exp);
    }

    public static DecisionDiagram createDiagram(BExp exp) {
        return new BExpVisitor<DecisionDiagram>() {
            @Override
            protected DecisionDiagram visit(Imp node) {
                DecisionDiagram cons = visit(node.getConsequent());
                return visit(node.getAntedecent()).compose(cons, DecisionDiagram.TRUE);
            }

            @Override
            protected DecisionDiagram visit(Or node) {
                DecisionDiagram right = visit(node.getRight());
                return visit(node.getLeft()).compose(DecisionDiagram.TRUE, right);
            }

            @Override
            protected DecisionDiagram visit(Not node) {
                return visit(node.getExp()).compose(DecisionDiagram.FALSE, DecisionDiagram.TRUE);
            }

            @Override
            protected DecisionDiagram visit(Var node) {
                return DecisionDiagram.choice(node.getName(), DecisionDiagram.TRUE, DecisionDiagram.FALSE);
            }
        }.visit(exp);
    }

}