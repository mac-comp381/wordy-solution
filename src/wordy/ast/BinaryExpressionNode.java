package wordy.ast;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

import wordy.interpreter.EvaluationContext;

import static wordy.ast.Utils.orderedMap;

/**
 * Two expressions joined by an operator (e.g. “x plus y”) in a Wordy abstract syntax tree.
 */
public class BinaryExpressionNode extends ExpressionNode {
    public enum Operator {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, EXPONENTIATION
    }

    private final Operator operator;
    private final ExpressionNode lhs, rhs;

    public BinaryExpressionNode(Operator operator, ExpressionNode lhs, ExpressionNode rhs) {
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return orderedMap(
            "lhs", lhs,
            "rhs", rhs);
    }

    @Override
    public double doEvaluate(EvaluationContext context) {
        double lhsValue = lhs.evaluate(context);
        double rhsValue = rhs.evaluate(context);

        switch(operator) {
            case ADDITION:
                return lhsValue + rhsValue;
            case SUBTRACTION:
                return lhsValue - rhsValue;
            case MULTIPLICATION:
                return lhsValue * rhsValue;
            case DIVISION:
                return lhsValue / rhsValue;
            case EXPONENTIATION:
                return Math.pow(lhsValue, rhsValue);
            default:
                throw new UnsupportedOperationException("Unknown operator: " + operator);
        }
    }

    @Override
    public void compile(PrintWriter out) {
        if(operator == Operator.EXPONENTIATION) {
            out.print("Math.pow(");
            lhs.compile(out);
            out.print(",");
            rhs.compile(out);
            out.print(")");
            return;
        }

        out.print("(");
        lhs.compile(out);
        out.print(
            switch(operator) {
                case ADDITION       -> "+";
                case SUBTRACTION    -> "-";
                case MULTIPLICATION -> "*";
                case DIVISION       -> "/";
                default -> throw new UnsupportedOperationException("Unknown operator: " + operator);
            }
        );
        rhs.compile(out);
        out.print(")");

        // The “switch expression” above is a relatively new Java feature. It’s nice!
        // Both a regular switch and chained if/else statements are also good solutions.
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        BinaryExpressionNode that = (BinaryExpressionNode) o;
        return this.operator == that.operator
            && this.lhs.equals(that.lhs)
            && this.rhs.equals(that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, lhs, rhs);
    }

    @Override
    public String toString() {
        return "BinaryExpressionNode{"
            + "operator=" + operator
            + ", lhs=" + lhs
            + ", rhs=" + rhs
            + '}';
    }

    @Override
    protected String describeAttributes() {
        return "(operator=" + operator + ')';
    }
}
