package week11.eksamdemo.funlist.ast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FunlistProg extends FunlistNode {

    private List<FunlistFun> funs;

    public FunlistProg() {
        this.funs = new LinkedList<>();
    }

    public FunlistProg(List<FunlistFun> funs) {
        this.funs = funs;
    }

    public FunlistProg(FunlistFun... funs) {
        this(Arrays.asList(funs));
    }

    public List<FunlistFun> getFuns() {
        return funs;
    }

    public void addFun(FunlistFun fun) {
        funs.add(fun);
    }

    @Override
    public String toString() {
        return funs.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ", "prog(", ")"));
    }

    @Override
    public <T> T accept(FunlistAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
