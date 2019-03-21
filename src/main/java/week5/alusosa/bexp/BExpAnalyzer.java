package week5.alusosa.bexp;

import week5.alusosa.bexp.bexpAst.BExp;

import java.util.Set;

public class BExpAnalyzer {

    // Analüüsida avaldist kasutades Stats klassi meetodid.
    public static Stats analyze(BExp exp) {
        return null;
    }

    // Väärtustada avaldis, kui ette antud on tõeste muutujate hulk.
    public static boolean eval(BExp exp, Set<Character> tv) {
        return false;
    }

    // Teisendada avaldises implikatsiooni teiste operaatoritega.
    public static BExp transform(BExp exp) {
        return null;
    }

    // Teha avaldisest otsustuspuu.
    public static DecisionDiagram createDiagram(BExp exp) {
        return null;
    }

}