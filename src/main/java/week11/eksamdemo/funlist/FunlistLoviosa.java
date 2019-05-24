package week11.eksamdemo.funlist;

import cma.CMaProgram;
import week11.eksamdemo.funlist.ast.FunlistNode;

import java.util.Map;

public class FunlistLoviosa {

    // Genereerida iga funktsioonile vastav CMa programm!
    public static Map<String, CMaProgram> codeGen(FunlistNode prog) {
        throw new UnsupportedOperationException();
    }

    // Defineerime nüüde selline liides:
    public interface IntVarargOperator {
        int apply(int... args);
    }

    // Nüüd tuleks selle keele funktsioone teisendada ülalolevat liidest
    // rahuldava klassi isendiks. Eeldame, et programmis on ainult üks funktsioon.
    public static IntVarargOperator toJavaFun(FunlistNode node) {
        throw new UnsupportedOperationException();
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
