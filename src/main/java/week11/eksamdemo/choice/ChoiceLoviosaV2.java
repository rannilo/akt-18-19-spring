package week11.eksamdemo.choice;

import cma.*;
import week11.eksamdemo.choice.choiceAst.*;
import week11.eksamdemo.choice.choiceAst.ChoiceVisitor;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.JUMP;
import static cma.instruction.CMaLabelInstruction.Code.JUMPZ;

public class ChoiceLoviosaV2 {

    private static final int COUNTER_INDEX = 0;

    public static CMaProgram codeGen(ChoiceNode expr) {
        CMaProgramWriter pw = new CMaProgramWriter();
        pw.visit(LOADC, 0);
        pw.visit(STOREA, COUNTER_INDEX);
        pw.visit(POP);

        ChoiceVisitor<Void> visitor = new ChoiceVisitor<Void>() {
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
                CMaLabel _true = new CMaLabel();
                CMaLabel _end = new CMaLabel();
                pw.visit(LOADA, COUNTER_INDEX);
                pw.visit(LOADC, 1);
                pw.visit(ADD);
                pw.visit(STOREA, COUNTER_INDEX);
                pw.visit(LOAD); //counter kohalt choice otsus

                pw.visit(JUMPZ, _true);
                visit(decision.getTrueChoice());
                pw.visit(JUMP, _end);

                pw.visit(_true);
                visit(decision.getFalseChoice());
                pw.visit(_end);
                return null;
            }
        };
        visitor.visit(expr);
        return pw.toProgram();
    }

    // Lõpuks luua selline AST, kus liitmist ei esine. Tulemus peaks olema sama nagu
    // esialgne program, kui tehakse samu juhuslike valikuid.
    public static ChoiceNode optimize(ChoiceNode expr) {
        ChoiceVisitor<ChoiceNode> visitor = new ChoiceVisitor<ChoiceNode>() {
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
                return new ChoiceDecision(visit(decision.getTrueChoice()), visit(decision.getFalseChoice()));
            }
        };
        return visitor.visit(expr);
    }


    private static ChoiceNode concat(ChoiceNode left, ChoiceNode right) { //tagastab kahe summa
        ChoiceVisitor<ChoiceNode> visitor = new ChoiceVisitor<ChoiceNode>() {
            @Override
            protected ChoiceNode visit(ChoiceValue value) {
                return ChoiceAlusosa.addConst(right, value.getValue());
            }

            @Override
            protected ChoiceNode visit(ChoiceAdd add) {
                return null;
            }

            @Override
            protected ChoiceNode visit(ChoiceDecision decision) {
                visit(decision.getFalseChoice());
                visit(decision.getTrueChoice());
                return null;
            }
        };
        visitor.visit(left);
        return null;
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
