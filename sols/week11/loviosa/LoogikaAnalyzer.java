package week11.loviosa;

import cma.*;
import week9.pohiosa.loogika.loogikaAst.KuiSiisMuidu;
import week9.pohiosa.loogika.loogikaAst.LoogikaNode;
import week9.pohiosa.loogika.loogikaAst.LoogikaVisitor;
import week9.pohiosa.loogika.loogikaAst.aatomid.LoogikaLiteral;
import week9.pohiosa.loogika.loogikaAst.aatomid.LoogikaMuutuja;
import week9.pohiosa.loogika.loogikaAst.operaatorid.JaNode;
import week9.pohiosa.loogika.loogikaAst.operaatorid.VoiNode;
import week9.pohiosa.loogika.loogikaAst.operaatorid.VordusNode;

import java.io.IOException;
import java.util.*;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.LOADA;
import static cma.instruction.CMaIntInstruction.Code.LOADC;
import static cma.instruction.CMaLabelInstruction.Code.JUMP;
import static cma.instruction.CMaLabelInstruction.Code.JUMPZ;
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
        LoogikaVisitor<Boolean> visitor = new LoogikaVisitor<Boolean>() {
            @Override
            public Boolean visit(LoogikaLiteral literal) {
                return literal.isValue();
            }

            @Override
            public Boolean visit(LoogikaMuutuja muutuja) {
                return env.get(muutuja.getNimi());
            }

            @Override
            public Boolean visit(JaNode ja) {
                return visit(ja.getLeftChild()) && (boolean) visit(ja.getRightChild());
            }

            @Override
            public Boolean visit(VoiNode voi) {
                return visit(voi.getLeftChild()) || (boolean) visit(voi.getRightChild());
            }

            @Override
            public Boolean visit(VordusNode vordus) {
                return visit(vordus.getLeftChild()) == (boolean) visit(vordus.getRightChild());
            }

            @Override
            public Boolean visit(KuiSiisMuidu kuiSiisMuidu) {
                if (visit(kuiSiisMuidu.getKuiAvaldis())) {
                    return visit(kuiSiisMuidu.getSiisAvaldis());
                } else if (kuiSiisMuidu.getMuiduAvaldis() != null) {
                    return visit(kuiSiisMuidu.getMuiduAvaldis());
                } else
                    return true;  // kui MUIDU avaldis on puudu, tagastame true
            }
        };

        return visitor.visit(node);
    }



    public static CMaProgram compile(LoogikaNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();

        LoogikaVisitor<Void> visitor = new LoogikaVisitor<Void>() {
            @Override
            public Void visit(LoogikaLiteral literal) {
                pw.visit(LOADC, CMaUtils.bool2int(literal.isValue()));
                return null;
            }

            @Override
            public Void visit(LoogikaMuutuja muutuja) {
                pw.visit(LOADA, MUUTUJAD.indexOf(muutuja.getNimi()));
                return null;
            }

            @Override
            public Void visit(JaNode ja) {
                visit(ja.getLeftChild());
                visit(ja.getRightChild());
                pw.visit(AND);
                return null;
            }

            @Override
            public Void visit(VoiNode voi) {
                visit(voi.getLeftChild());
                visit(voi.getRightChild());
                pw.visit(OR);
                return null;
            }

            @Override
            public Void visit(VordusNode vordus) {
                visit(vordus.getLeftChild());
                visit(vordus.getRightChild());
                pw.visit(EQ);
                return null;
            }

            @Override
            public Void visit(KuiSiisMuidu kuiSiisMuidu) {
                CMaLabel _else = new CMaLabel();
                CMaLabel _endif = new CMaLabel();
                visit(kuiSiisMuidu.getKuiAvaldis());
                pw.visit(JUMPZ, _else);
                visit(kuiSiisMuidu.getSiisAvaldis());
                pw.visit(JUMP, _endif);
                pw.visit(_else);
                if (kuiSiisMuidu.getMuiduAvaldis() != null)
                    visit(kuiSiisMuidu.getMuiduAvaldis());
                else
                    pw.visit(LOADC, CMaUtils.bool2int(true));
                pw.visit(_endif);
                return null;
            }
        };

        visitor.visit(node);

        return pw.toProgram();
    }
}
