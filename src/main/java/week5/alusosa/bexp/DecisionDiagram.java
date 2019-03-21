package week5.alusosa.bexp;

import java.util.Set;

public abstract class DecisionDiagram {

    public static DecisionDiagram TRUE = new TrueNode();
    public static DecisionDiagram FALSE = new FalseNode();

    public static DecisionDiagram choice(char c, DecisionDiagram tc, DecisionDiagram fc) {
        return new ChoiceNode(c, tc, fc);
    }

    public abstract boolean eval(Set<Character> tv);
    public abstract DecisionDiagram compose(DecisionDiagram trueDecision, DecisionDiagram falseDecision);


    private static class ChoiceNode extends DecisionDiagram {
        private Character variable;
        private DecisionDiagram trueChoice;
        private DecisionDiagram falseChoice;

        public ChoiceNode(Character variable, DecisionDiagram trueChoice, DecisionDiagram falseChoice) {
            this.variable = variable;
            this.trueChoice = trueChoice;
            this.falseChoice = falseChoice;
        }

        @Override
        public boolean eval(Set<Character> tv) {
            return tv.contains(variable) ? trueChoice.eval(tv) : falseChoice.eval(tv);
        }

        @Override
        public DecisionDiagram compose(DecisionDiagram trueDecision, DecisionDiagram falseDecision) {
            return new ChoiceNode(variable,
                    trueChoice.compose(trueDecision, falseDecision),
                    falseChoice.compose(trueDecision, falseDecision));
        }

        @Override
        public String toString() {
            return "(" + variable + " ? " + trueChoice + " : " + falseChoice + ")";
        }
    }

    private static class TrueNode extends DecisionDiagram {
        @Override
        public boolean eval(Set<Character> tv) {
            return true;
        }

        @Override
        public DecisionDiagram compose(DecisionDiagram trueDecision, DecisionDiagram falseDecision) {
            return trueDecision;
        }

        @Override
        public String toString() {
            return "true";
        }
    }

    private static class FalseNode extends DecisionDiagram {
        @Override
        public boolean eval(Set<Character> tv) {
            return false;
        }

        @Override
        public DecisionDiagram compose(DecisionDiagram trueDecision, DecisionDiagram falseDecision) {
            return falseDecision;
        }

        @Override
        public String toString() {
            return "false";
        }
    }

}
