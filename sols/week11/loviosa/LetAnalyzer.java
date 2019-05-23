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
        LetVisitor<Boolean> visitor = new LetVisitor<Boolean>() {
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
                         //&& onVaja(sidumine.getMuutujaNimi(), sidumine.getKeha())  // Ainult vaja, kui päring tehakse optimeerimata puu peal.
                        || !sidumine.getMuutujaNimi().equals(vajaMuutuja) && visit(sidumine.getKeha());
            }

            @Override
            protected Boolean visit(LetSumma summa) {
                return visit(summa.getLo()) || visit(summa.getHi()) ||
                        !summa.getMuutujaNimi().equals(vajaMuutuja) && visit(summa.getKeha());
            }

            @Override
            protected Boolean visit(LetVahe vahe) {
                return visit(vahe.getVasak()) || visit(vahe.getParem());
            }
        };

        return visitor.visit(avaldis);
    }

    public static LetAvaldis optimeeri(LetAvaldis avaldis) {
        LetVisitor<LetAvaldis> visitor = new LetVisitor<LetAvaldis>() {
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
                LetAvaldis keha = visit(sidumine.getKeha());
                if (onVaja(sidumine.getMuutujaNimi(), keha)) {
                    LetAvaldis sisu = visit(sidumine.getMuutujaSisu());
                    return new LetSidumine(sidumine.getMuutujaNimi(), sisu, keha);
                } else return keha;
            }

            @Override
            protected LetAvaldis visit(LetSumma summa) {
                LetAvaldis keha = visit(summa.getKeha());
                LetAvaldis alam = visit(summa.getLo());
                LetAvaldis ylem = visit(summa.getHi());
                return new LetSumma(summa.getMuutujaNimi(), alam, ylem, keha);
            }

            @Override
            protected LetAvaldis visit(LetVahe vahe) {
                return new LetVahe(visit(vahe.getVasak()), visit(vahe.getParem()));
            }
        };

        return visitor.visit(avaldis);
    }

    // Vana hea eval meetod... tuleb avaldist väärtustada!
    public static int eval(LetAvaldis expr) {
        LetVisitor<Integer> visitor = new LetVisitor<Integer>() {
            private final IntegerEnvironment env = new IntegerEnvironment();

            @Override
            protected Integer visit(LetArv arv) {
                return arv.getArv();
            }

            @Override
            protected Integer visit(LetMuutuja muutuja) {
                return env.get(muutuja.getNimi());
            }

            @Override
            protected Integer visit(LetSidumine sidumine) {
                Integer value = visit(sidumine.getMuutujaSisu());
                env.enterBlock();
                env.declare(sidumine.getMuutujaNimi());
                env.assign(sidumine.getMuutujaNimi(), value);
                Integer result = visit(sidumine.getKeha());
                env.exitBlock();
                return result;
            }

            @Override
            protected Integer visit(LetSumma summa) {
                int from = visit(summa.getLo());
                int to = visit(summa.getHi());
                int result = 0;
                for (int i = from; i <= to; i++) {
                    env.enterBlock();
                    env.declare(summa.getMuutujaNimi());
                    env.assign(summa.getMuutujaNimi(), i);
                    result += visit(summa.getKeha());
                    env.exitBlock();
                }
                return result;
            }

            @Override
            protected Integer visit(LetVahe vahe) {
                return visit(vahe.getVasak()) - visit(vahe.getParem());
            }
        };

        return visitor.visit(expr);
    }


    private static List<String> VARS = Arrays.asList("x", "y", "z");

    // Koodi genereerimiseks eeldame, et let'id ja summad ei esine alamavaldistena!
    // Meil on lisaks mõned globaalsed muutujad x, y, z, mis asuvad pesades 0, 1 ja 2.
    public static CMaProgram codeGen(LetAvaldis expr) {
        CMaProgramWriter pw = new CMaProgramWriter();

        Map<String, Integer> env = new HashMap<>();
        for (String var : VARS) env.put(var, VARS.indexOf(var));

        new LetVisitor<Void>() {
            int SP = env.size();

            @Override
            protected Void visit(LetArv arv) {
                pw.visit(LOADC, arv.getArv());
                return null;
            }

            @Override
            protected Void visit(LetMuutuja muutuja) {
                pw.visit(LOADA, env.get(muutuja.getNimi()));
                return null;
            }

            @Override
            protected Void visit(LetSidumine sidumine) {
                String nimi = sidumine.getMuutujaNimi();
                visit(sidumine.getMuutujaSisu());
                env.put(nimi, SP++);
                visit(sidumine.getKeha());
                return null;
            }

            @Override
            protected Void visit(LetSumma summa) {
                // avaldis kujul idx = lo to hi in ...idx...

                // Alustame idx = lo
                String nimi = summa.getMuutujaNimi();
                visit(summa.getLo());
                int idx = SP++;
                env.put(nimi, idx);

                visit(summa.getHi());
                int high = SP++;
                CMaLabel _while = new CMaLabel();
                CMaLabel _exit = new CMaLabel();

                // Tulemuse salvestamiseks:
                pw.visit(LOADC, 0);

                // while (idx <= high)
                pw.visit(_while);
                pw.visit(LOADA, idx);
                pw.visit(LOADA, high);
                pw.visit(LEQ);
                pw.visit(JUMPZ, _exit);

                // result += keha;
                visit(summa.getKeha());
                pw.visit(ADD);

                // idx++;
                pw.visit(LOADA, idx);
                pw.visit(LOADC, 1);
                pw.visit(ADD);
                pw.visit(STOREA, idx);
                pw.visit(POP);

                pw.visit(JUMP, _while);

                pw.visit(_exit);

                return null;
            }

            @Override
            protected Void visit(LetVahe vahe) {
                visit(vahe.getVasak());
                visit(vahe.getParem());
                pw.visit(SUB);
                return null;
            }
        }.visit(expr);

        return pw.toProgram();
    }


    public static void main(String[] args) {
        LetAvaldis avaldis = let("a",num(666),let("b",vahe(var("a"),num(1)),num(3)));
        System.out.println(avaldis);
        System.out.println(optimeeri(avaldis));
        System.out.println(eval(avaldis));
        System.out.println(CMaInterpreter.run(codeGen(avaldis), new CMaStack(0, 0, 0)));
    }

}
