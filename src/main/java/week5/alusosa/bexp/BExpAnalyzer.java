package week5.alusosa.bexp;

import week5.alusosa.bexp.bexpAst.BExp;

import java.util.Set;

public class BExpAnalyzer {

    // Analüüsida avaldist kasutades Stats klassi meetodid.
    public static Stats analyze(BExp exp) {
        //Boolean visitor, mis tagastab tõeväärtuse olenevalt kas puus on implikatsioon
        //Set<Character> visitor, mis tagastab kõikide muutujate nimed
        return null;
    }

    // Väärtustada avaldis, kui ette antud on tõeste muutujate hulk.
    public static boolean eval(BExp exp, Set<Character> tv) {
        //Boolean visitor, kui on lehes siis tagastab true kui char kuulub tv hulka, ülejäänu töötab nagu ikka
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