package wordy.ast;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import wordy.interpreter.EvaluationContext;

/**
 * A sequence of zero or more sequentially executed statements in a Wordy abstract syntax tree.
 */
public class BlockNode extends StatementNode {
    /**
     * A BlockNode containing zero statements.
     */
    public static final BlockNode EMPTY = new BlockNode();

    private final List<StatementNode> statements;

    public BlockNode(List<StatementNode> statements) {
        this.statements = List.copyOf(statements);
    }

    public BlockNode(StatementNode... statements) {
        this.statements = Arrays.asList(statements);
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        Map<String, ASTNode> result = new LinkedHashMap<>();
        var iter = statements.iterator();
        for(int index = 0; iter.hasNext(); index++) {
            result.put(String.valueOf(index), iter.next());
        }
        return result;
    }

    @Override
    public void doRun(EvaluationContext context) {
        for(var statement: statements)
            statement.run(context);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        BlockNode blockNode = (BlockNode) o;
        return statements.equals(blockNode.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statements);
    }

    @Override
    public String toString() {
        return "BlockNode{statements=" + statements + '}';
    }

    @Override
    protected String describeAttributes() {
        return "(%d %s)"
            .formatted(statements.size(), statements.size() == 1 ? "child" : "children");
    }
}
