package eksam2;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import eksam2.ast.BologImp;
import eksam2.ast.BologNode;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BologLoviosa {

    private static final List<String> VARS = Arrays.asList("P", "Q", "R", "S", "T", "U", "V"); // kasuta VARS.indexOf

    public static CMaProgram codeGen(BologNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();
        return pw.toProgram();
    }


    // Leia muutujate hulk, mis peavad olema tõesed selleks, et kõik etteantud atomaarsed implikatsioonid oleksid tõesed.
    // Selles ülesandes võib olla valemis väga suur arv muutujad, et kõikide väärtuste läbivaatamine ei toimi.
    public static Set<String> leastModel(Set<BologImp> imps) {
        throw new UnsupportedOperationException();
    }
}
