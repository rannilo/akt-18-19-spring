package week11.loviosa;

import cma.*;
import week10.IntegerEnvironment;
import week9.pohiosa.letex.letAst.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.JUMP;
import static cma.instruction.CMaLabelInstruction.Code.JUMPZ;
import static week9.pohiosa.letex.letAst.LetAvaldis.*;

public class LetAnalyzer {


    public static boolean onVaja(String vajaMuutuja, LetAvaldis avaldis) {
        return new LetVisitor<Boolean>() {
            @Override
            protected Boolean visit(LetArv arv) {
                return false;
            }

            @Override
            protected Boolean visit(LetMuutuja muutuja) {
                return muutuja.getNimi().equals(vajaMuutuja);
            }

            @Override
            protected Boolean visit(LetSidumine sidumine) {
                return visit(sidumine.getMuutujaSisu())
                        || !sidumine.getMuutujaNimi().equals(vajaMuutuja) && visit(sidumine.getKeha());
            }

            @Override
            protected Boolean visit(LetSumma summa) {
                return visit(summa.getLo()) || visit(summa.getHi()) ||
                        !summa.getMuutujaNimi().equals(vajaMuutuja) && visit(summa.getKeha());
            }

            @Override
            protected Boolean visit(LetVahe vahe) {
                return visit(vahe.getParem()) || visit(vahe.getVasak());
            }
        }.visit(avaldis);
    }

    public static LetAvaldis optimeeri(LetAvaldis avaldis) {
        return new LetVisitor<LetAvaldis>() {
            @Override
            protected LetAvaldis visit(LetArv arv) {
                return arv;
            }

            @Override
            protected LetAvaldis visit(LetMuutuja muutuja) {
                return muutuja;
            }

            @Override
            protected LetAvaldis visit(LetSidumine sidumine) {
                String nimi = sidumine.getMuutujaNimi();
                return null;
            }

            @Override
            protected LetAvaldis visit(LetSumma summa) {
                return summa;
            }

            @Override
            protected LetAvaldis visit(LetVahe vahe) {
                return vahe;
            }
        }.visit(avaldis);
    }

    // Vana hea eval meetod... tuleb avaldist väärtustada!
    public static int eval(LetAvaldis expr) {
        throw new UnsupportedOperationException();
    }


    private static List<String> VARS = Arrays.asList("x", "y", "z");

    // Koodi genereerimiseks eeldame, et let'id ja summad ei esine alamavaldistena!
    // Meil on lisaks mõned globaalsed muutujad x, y, z, mis asuvad pesades 0, 1 ja 2.
    public static CMaProgram codeGen(LetAvaldis expr) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }


    public static void main(String[] args) {
        LetAvaldis avaldis = let("a", num(666), let("b", vahe(var("a"), num(1)), num(3)));
        System.out.println(avaldis);
        System.out.println(optimeeri(avaldis));
        System.out.println(eval(avaldis));
        System.out.println(CMaInterpreter.run(codeGen(avaldis), new CMaStack(0, 0, 0)));
    }

}
