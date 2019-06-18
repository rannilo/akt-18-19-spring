package eksam2;

import cma.CMaLabel;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import cma.CMaUtils;
import eksam2.ast.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.LOADA;
import static cma.instruction.CMaIntInstruction.Code.LOADC;
import static cma.instruction.CMaLabelInstruction.Code.JUMPZ;

public class BologLoviosa {

    private static final List<String> VARS = Arrays.asList("P", "Q", "R", "S", "T", "U", "V"); // kasuta VARS.indexOf

    public static CMaProgram codeGen(BologNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();
        BologAstVisitor<Void> visitor = new BologAstVisitor<>(){
            @Override
            public Void visit(BologLit lit){
                pw.visit(LOADC, CMaUtils.bool2int(lit.isTrue()));
                return null;
            }
            @Override
            public Void visit(BologVar var){
                int i = VARS.indexOf(var.getName());
                pw.visit(LOADC, i);
                pw.visit(LOAD);
                return null;
            }
            @Override
            public Void visit(BologNand nand){
                visit(nand.getLeftExpr());
                visit(nand.getRightExpr());
                pw.visit(AND);
                pw.visit(NOT);
                return null;
            }
            @Override
            public Void visit(BologImp imp){
                CMaLabel _end = new CMaLabel();
                visit(imp.getConclusion());
                pw.visit(JUMPZ, _end);
                //kui stacki peal on tõene, siis mine lõppu
                //kui stacki peal on väär, siis jätka
                pw.visit(POP); //eemaldan järelduse stackilt
                for(BologNode a: imp.getAssumptions()){
                    visit(a);
                }
                for(int i = 0; i<imp.getAssumptions().size()-1; i++){
                    pw.visit(AND);
                }
                pw.visit(NOT);
                pw.visit(_end);
                return null;
            }
        };
        visitor.visit(node);
        return pw.toProgram();
    }


    // Leia muutujate hulk, mis peavad olema tõesed selleks, et kõik etteantud atomaarsed implikatsioonid oleksid tõesed.
    // Selles ülesandes võib olla valemis väga suur arv muutujad, et kõikide väärtuste läbivaatamine ei toimi.
    public static Set<String> leastModel(Set<BologImp> imps) {
        Set<String> muutujad = new HashSet<>();
        BologAstVisitor<String> visitor = new BologAstVisitor<String>(){
            @Override
            public String visit(BologLit lit){
                return Boolean.toString(lit.isTrue());
            }
            @Override
            public String visit(BologVar var){
                return var.getName();
            }
            @Override
            public String visit(BologImp imp){
                String conc = visit(imp.getConclusion());
                if(!conc.equals("false") && !conc.equals("true")){ //kui muutuja on true, siis imp on alati true
                    muutujad.add(conc);
                }
                else if(conc.equals("false")){ //kui on false, vaatame kõik eeldused läbi
                    for(BologNode n: imp.getAssumptions()){
                        String string = visit(n);
                        if(!string.equals("true") && !string.equals("false")){
                            muutujad.add(string);
                        }
                    }
                }
                return null;
            }
        };
        for(BologImp imp: imps){
            visitor.visit(imp);
        }
        return muutujad;
    }
}
