package week11.eksamdemo.funlist;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import week11.eksamdemo.funlist.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cma.instruction.CMaBasicInstruction.Code.ADD;
import static cma.instruction.CMaIntInstruction.Code.LOADA;
import static cma.instruction.CMaIntInstruction.Code.LOADC;

public class FunlistLoviosa {

    // Genereerida iga funktsioonile vastav CMa programm!
    public static Map<String, CMaProgram> codeGen(FunlistNode prog) {
        Map<String, CMaProgram> programs = new HashMap<>();
        new FunlistAstVisitor<Void>(){
            CMaProgramWriter pw;
            List<Character> params;
            @Override
            protected Void visit(FunlistLit lit){
                pw.visit(LOADC, lit.getVal());
                return null;
            }
            @Override
            protected Void visit(FunlistVar var){
                pw.visit(LOADA, params.indexOf(var.getName()));
                return null;
            }
            @Override
            protected Void visit(FunlistAdd add){
                visit(add.getLeft());
                visit(add.getRight());
                pw.visit(ADD);
                return null;
            }
            @Override
            protected Void visit(FunlistFun fun){
                pw = new CMaProgramWriter();
                params = fun.getParams();
                visit(fun.getBody());
                programs.put(fun.getName(), pw.toProgram());
                return null;
            }
        }.visit(prog);
        return programs;
    }

    // Defineerime nüüde selline liides:
    public interface IntVarargOperator {
        int apply(int... args);
    }

    // Nüüd tuleks selle keele funktsioone teisendada ülalolevat liidest
    // rahuldava klassi isendiks. Eeldame, et programmis on ainult üks funktsioon.
    public static IntVarargOperator toJavaFun(FunlistNode node) {
        return new FunlistAstVisitor<IntVarargOperator>(){

            private List<Character> params;

            @Override
            protected IntVarargOperator visit(FunlistLit lit){
                return a -> lit.getVal();
            }

            @Override
            protected IntVarargOperator visit(FunlistVar var){
                return a -> a[params.indexOf(var.getName())];
            }

            @Override
            protected IntVarargOperator visit(FunlistAdd add){
                return a -> visit(add.getLeft()).apply(a) + visit(add.getRight()).apply(a);
            }

            @Override
            protected IntVarargOperator visit(FunlistFun fun){
                params = fun.getParams();
                return visit(fun.getBody());
            }
        }.visit(node);
    }

    public static void main(String[] args) {

        // IntelliJ peaks soovitama seda lambda-ks teisendada!
        IntVarargOperator f = new IntVarargOperator() {
            @Override
            public int apply(int... a) {
                return a[0] + a[1] + a[2];
            }
        };

        System.out.println(f.apply(1, 2, 3));
    }

}
