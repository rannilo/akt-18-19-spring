package eksam1.ast;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class CondProg extends CondNode {

    private List<CondDecl> decls;
    private CondNode expression;

    public CondProg(List<CondDecl> decls, CondNode expression) {
        this.decls = decls;
        this.expression = expression;
    }

    public CondNode getExpression() {
        return expression;
    }

    public List<CondDecl> getDecls() {
            return decls;
    }

    @Override
    public String toString() {
        String declStr = decls.stream()
                .map(CondDecl::toString)
                .collect(joining(", ", "decls(", ")"));
        return "prog("+ declStr + ", " + expression + ")";
    }

    @Override
    public <T> T accept(CondAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
