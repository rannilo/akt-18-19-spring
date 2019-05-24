package week11.eksamdemo.funlist.ast;

public abstract class FunlistNode {

    // Võimaldavad natuke mugavamalt luua neid avalidisi testimiseks.
    // ASTi loomiseks on FunlistFun ja FunlistProg konstruktorid & addFun meetoditega mugavam.
    public static FunlistNode lit(int i) {
        return new FunlistLit(i);
    }
    public static FunlistNode var(char x) {
        return new FunlistVar(x);
    }
    public static FunlistNode add(FunlistNode e1, FunlistNode e2) {
        return new FunlistAdd(e1, e2);
    }

    public static FunlistFun fun(String name, FunlistNode body, Character... params) {
        return new FunlistFun(name, body, params);
    }

    public static FunlistProg prog(FunlistFun... funs) {
        return new FunlistProg(funs);
    }


    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FunlistNode)) return false;
        return toString().equals(obj.toString());
    }

    // Visitori implementatsiooniks:
    public abstract <T> T accept(FunlistAstVisitor<T> visitor);

}
