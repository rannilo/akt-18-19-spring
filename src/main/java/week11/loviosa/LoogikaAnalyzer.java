package week11.loviosa;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import cma.CMaStack;
import week9.pohiosa.loogika.loogikaAst.LoogikaNode;

import java.io.IOException;
import java.util.*;

import static week9.pohiosa.loogika.loogikaAst.LoogikaNode.*;

public class LoogikaAnalyzer {


    private static final List<String> MUUTUJAD = Arrays.asList("x", "y", "z", "a", "b", "c"); // kasuta MUUTUJAD.indexOf
    private static final List<Integer> VAARTUSED = Arrays.asList(0, 1, 1, 1, 0, 1);

    public static void main(String[] args) throws IOException {
        LoogikaNode node = kuiSiis(var("a"), kuiSiis(var("a"), lit(true), lit(false)), lit(false));

        // Eihtame väärtuste keskkonda ja väärtustame
        Map<String, Boolean> env = new HashMap<>();
        Iterator<String> i1 = MUUTUJAD.iterator();
        Iterator<Integer> i2 = VAARTUSED.iterator();
        while (i1.hasNext()) env.put(i1.next(), i2.next() == 1);
        System.out.println(eval(node, env));

        // kompileeri avaldist arvutav CMa programm
        CMaProgram program = compile(node);

        // kirjuta programm faili, mida saab Vam-iga vaadata, koos muutujate väärtustega stackil
        CMaStack initialStack = new CMaStack(VAARTUSED); // MUUTUJATE VÄÄRTUSED
        program.toFile("loogika.cma", initialStack);

        // interpreteeri CMa programm
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);
        System.out.println(finalStack.peek());
    }


    // See eksami alusosa ülesanne võib soojenduseks implementeerida...
    public static boolean eval(LoogikaNode node, Map<String, Boolean> env) {
        throw new UnsupportedOperationException();
    }



    public static CMaProgram compile(LoogikaNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }
}
