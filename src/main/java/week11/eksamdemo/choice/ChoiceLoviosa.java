package week11.eksamdemo.choice;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import cma.CMaStack;
import week11.eksamdemo.choice.choiceAst.ChoiceNode;

public class ChoiceLoviosa {

    public static CMaProgram codeGen(ChoiceNode expr) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }

    // Lõpuks luua selline AST, kus liitmist ei esine. Tulemus peaks olema sama nagu
    // esialgne program, kui tehakse samu juhuslike valikuid.
    public static ChoiceNode optimize(ChoiceNode expr) {
        return expr;
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
