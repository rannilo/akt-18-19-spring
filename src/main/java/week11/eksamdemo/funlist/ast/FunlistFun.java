package week11.eksamdemo.funlist.ast;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FunlistFun extends FunlistNode {

    private final String name;
    private FunlistNode body;
    private List<Character> params;

    public FunlistFun(String name, FunlistNode body, List<Character> params) {
        this.name = name;
        this.body = body;
        this.params = params;
    }

    public FunlistFun(String name, FunlistNode body, Character[] params) {
        this(name, body, Arrays.asList(params));
    }

    public String getName() {
        return name;
    }

    public List<Character> getParams() {
        return params;
    }

    public FunlistNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        String parstr = params.stream()
                .map(c -> ", '" + c + "'")
                .collect(Collectors.joining());
        return "fun(\"" + name + "\", " + body + parstr + ")";
    }

    @Override
    public <T> T accept(FunlistAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
