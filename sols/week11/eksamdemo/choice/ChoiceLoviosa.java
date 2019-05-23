package week11.eksamdemo.choice;

import cma.*;
import cma.instruction.CMaLabelInstruction;
import week11.eksamdemo.choice.choiceAst.ChoiceVisitor;
import week11.eksamdemo.choice.choiceAst.*;

import static cma.instruction.CMaBasicInstruction.Code.ADD;
import static cma.instruction.CMaIntInstruction.Code.LOADA;
import static cma.instruction.CMaIntInstruction.Code.LOADC;
import static cma.instruction.CMaLabelInstruction.Code.JUMP;

public class ChoiceLoviosa {

    public static CMaProgram codeGen(ChoiceNode expr) {
        CMaProgramWriter pw = new CMaProgramWriter();

        new ChoiceVisitor<Void>() {
            int choiceCount = 0;

            @Override
            protected Void visit(ChoiceValue value) {
                pw.visit(LOADC, value.getValue());
                return null;
            }

            @Override
            protected Void visit(ChoiceAdd add) {
                visit(add.getLeft());
                visit(add.getRight());
                pw.visit(ADD);
                return null;
            }

            @Override
            protected Void visit(ChoiceDecision decision) {
                CMaLabel _false = new CMaLabel();
                CMaLabel _done = new CMaLabel();
                pw.visit(LOADA, choiceCount++);
                pw.visit(CMaLabelInstruction.Code.JUMPZ, _false);

                int oldCount = choiceCount;
                visit(decision.getTrueChoice());
                pw.visit(JUMP, _done);

                pw.visit(_false);
                choiceCount = oldCount;
                visit(decision.getFalseChoice());
                pw.visit(_done);
                return null;
            }
        }.visit(expr);

        return pw.toProgram();
    }

    // Lõpuks luua selline AST, kus liitmist ei esine. Tulemus peaks olema sama nagu
    // esialgne program, kui tehakse samu juhuslike valikuid.
    public static ChoiceNode optimize(ChoiceNode expr) {
        return new ChoiceVisitor<ChoiceNode>() {
            @Override
            protected ChoiceNode visit(ChoiceValue value) {
                return value;
            }

            @Override
            protected ChoiceNode visit(ChoiceAdd add) {
                return concat(visit(add.getLeft()), visit(add.getRight()));
            }

            @Override
            protected ChoiceNode visit(ChoiceDecision decision) {
                return ChoiceNode.choice(
                        visit(decision.getTrueChoice()),
                        visit(decision.getFalseChoice()));
            }
        }.visit(expr);
    }

    private static ChoiceNode concat(ChoiceNode left, ChoiceNode right) {
        return new ChoiceVisitor<ChoiceNode>() {
            @Override
            protected ChoiceNode visit(ChoiceValue value) {
                return ChoiceAlusosa.addConst(right, value.getValue());
            }

            @Override
            protected ChoiceNode visit(ChoiceAdd add) {
                throw new IllegalArgumentException("Should only be called on optimized trees.");
            }

            @Override
            protected ChoiceNode visit(ChoiceDecision decision) {
                return ChoiceNode.choice(
                        visit(decision.getTrueChoice()),
                        visit(decision.getFalseChoice())
                );
            }
        }.visit(left);
    }

    public static void main(String[] args) {
        ChoiceNode expr = ChoicePohiosa.makeChoiceAst("(1|3)+(10|20)");

        System.out.println(expr);
        CMaProgram prog = codeGen(expr);
        CMaStack stack = new CMaStack(1, 0);// (vasak, parem)
        CMaStack result = CMaInterpreter.run(prog, stack);

        System.out.println(result.peek());
        ChoiceNode optimized = optimize(expr);
        System.out.println(optimized);

        result = CMaInterpreter.run(codeGen(optimized), stack);
        System.out.println(result.peek());
    }

}
