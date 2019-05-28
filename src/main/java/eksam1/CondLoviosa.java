package eksam1;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import eksam1.ast.*;

import static eksam1.ast.CondNode.*;

public class CondLoviosa {

    public static CMaProgram codeGen(CondNode prog) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }


    public static CondNode symbex(CondNode prog) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        CondNode expr = ifte(eq(var("x"), il(10)), var("error"), var("good"));
        System.out.println(symbex(expr));
    }
}
