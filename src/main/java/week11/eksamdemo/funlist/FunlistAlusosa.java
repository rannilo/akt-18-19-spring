package week11.eksamdemo.funlist;

import week11.eksamdemo.funlist.ast.FunlistNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static week11.eksamdemo.funlist.ast.FunlistNode.*;

public class FunlistAlusosa {

    // Lihtsalt tagastada kõik literaalid...
    public static Set<Integer> allLiterals(FunlistNode prog) {
        throw new UnsupportedOperationException();
    }

    // Tagastada funktsiooni, mis ei sõltu ühestki oma argumendist...
    public static Set<String> constFuns(FunlistNode ast) {
        throw new UnsupportedOperationException();
    }


    // (Viimase) funktsiooni väärtustamine globaalse keskkonnaga
    public static Integer eval(FunlistNode expr, Map<Character, Integer> env) {
        throw new UnsupportedOperationException();
    }

    // Ühe konkreetse funktsiooni väärtustamine. Lisaks globaalsele keskkonnale saab ka
    // konkreetsete funktsioonide argumente: katseta main meetodis, kui ei ole päris selge.
    public static int eval(FunlistNode prog, Map<Character, Integer> glob, String f, Integer... args) {
        // Integer... tüüpi muutuja kasutatakse nagu tavaline massiiv ja võib isegi omistada:
        // Integer[] argsArray = args;
        throw new UnsupportedOperationException();
    }

    // Üks näide selle viimase kohta
    public static void main(String[] args) {
        Map<Character, Integer> env = new HashMap<>();
        env.put('x', 10);
        env.put('y', 20);
        FunlistNode prog = prog(fun("addToY", add(var('x'), var('y')), 'x'));
        System.out.println(eval(prog, env)); // Tulemuseks 30 (loeme mõlemad keskkonnast
        System.out.println(eval(prog, env, "addToY", 5)); // Tulemuseks 25
    }

}
