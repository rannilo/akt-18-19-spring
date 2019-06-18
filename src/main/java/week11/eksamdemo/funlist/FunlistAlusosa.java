package week11.eksamdemo.funlist;

import week11.eksamdemo.funlist.ast.*;

import java.util.*;

import static week11.eksamdemo.funlist.ast.FunlistNode.*;

public class FunlistAlusosa {

    // Lihtsalt tagastada kõik literaalid...
    public static Set<Integer> allLiterals(FunlistNode prog) {
        Set<Integer> accumulator = new HashSet<>();
        new FunlistAstVisitor<Void>(){
            @Override
            protected Void visit(FunlistLit lit){
                accumulator.add(lit.getVal());
                return null;
            }
        }.visit(prog);
        return accumulator;
    }

    // Tagastada funktsiooni, mis ei sõltu ühestki oma argumendist...
    public static Set<String> constFuns(FunlistNode ast) {
        Set<String> funs = new HashSet<>();
        new FunlistAstVisitor<Set<Character>>(){
            Set<Character> vars = new HashSet<>();
            @Override
            protected Set<Character> visit(FunlistFun fun){
                if(fun.getParams().size() == 0){
                    funs.add(fun.getName());
                    return null;
                }
                Set<Character> set = visit(fun.getBody());
                if(Collections.disjoint(fun.getParams(), set)){
                    funs.add(fun.getName());
                }
                return null;
            }
            @Override
            protected Set<Character> visit(FunlistVar var){
                return Collections.singleton(var.getName());
            }
            @Override
            protected Set<Character> visit(FunlistLit lit){
                return Collections.emptySet();
            }
            @Override
            protected Set<Character> visit(FunlistAdd add){
                Set<Character> set = new HashSet<>();
                set.addAll(visit(add.getLeft()));
                set.addAll(visit(add.getRight()));
                return set;
            }
        }.visit(ast);
        return funs;
    }


    // (Viimase) funktsiooni väärtustamine globaalse keskkonnaga
    public static Integer eval(FunlistNode expr, Map<Character, Integer> env) {
        return new FunlistAstVisitor<Integer>(){
            @Override
            protected Integer visit(FunlistLit lit){
                return lit.getVal();
            }
            @Override
            protected Integer visit(FunlistVar var){
                return env.getOrDefault(var.getName(), 0);
            }
            @Override
            protected Integer visit(FunlistAdd add){
                return visit(add.getLeft()) + visit(add.getRight());
            }
        }.visit(expr);
    }

    // Ühe konkreetse funktsiooni väärtustamine. Lisaks globaalsele keskkonnale saab ka
    // konkreetsete funktsioonide argumente: katseta main meetodis, kui ei ole päris selge.
    public static int eval(FunlistNode prog, Map<Character, Integer> glob, String f, Integer... args) {
        // Integer... tüüpi muutuja kasutatakse nagu tavaline massiiv ja võib isegi omistada:
        // Integer[] argsArray = args;
        return new FunlistAstVisitor<Integer>(){
            @Override
            protected Integer visit(FunlistFun fun){
                Map<Character, Integer> env = new HashMap<>(glob);
                Iterator<Integer> argIter = Arrays.asList(args).iterator();
                List<Character> params = fun.getParams();
                for(Character param: params){
                    env.put(param, argIter.next());
                }
                return eval(fun.getBody(), env);
            }
        }.visit(prog);
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
