package wordy.ast;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import wordy.interpreter.EvaluationContext;

import wordy.interpreter.EvaluationContext;

/**
 * A literal floating-point value (e.g. “3.141”) in a Wordy abstract syntax tree.
 */
public final class ConstantNode extends ExpressionNode {
    private final double value;

    public ConstantNode(double value) {
        this.value = value;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return Collections.emptyMap();
    }

    @Override
    public double doEvaluate(EvaluationContext context) {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        ConstantNode other = (ConstantNode) o;
        return Double.compare(this.value, other.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ConstantNode{value=" + value + '}';
    }

    @Override
    protected String describeAttributes() {
        return "(value=" + value + ')';
    }
}
