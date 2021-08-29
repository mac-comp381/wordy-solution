package wordy.interpreter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static wordy.parser.WordyParser.parseExpression;
import static wordy.parser.WordyParser.parseProgram;
import static wordy.parser.WordyParser.parseStatement;

public class InterpreterTest {
    private final EvaluationContext context = new EvaluationContext();

    @Test
    void evaluateConstant() {
        assertEvaluationEquals(2001, "2001");
        assertEvaluationEquals(-3.5, "-3.5");
    }

    @Test
    void evaluateVariable() {
        context.set("question", 54);
        context.set("answer", 42);
        assertEvaluationEquals(54, "question");
        assertEvaluationEquals(42, "answer");
        assertEvaluationEquals(0, "fish");
    }

    @Test
    void evaluateBinaryExpression() {
        assertEvaluationEquals(5, "2 plus 3");
        assertEvaluationEquals(-1, "2 minus 3");
        assertEvaluationEquals(6, "2 times 3");
        assertEvaluationEquals(2.0 / 3, "2 divided by 3");
        assertEvaluationEquals(4, "2 squared");
        assertEvaluationEquals(8, "2 to the power of 3");
        assertEvaluationEquals(511.5, "2 to the power of 3 squared minus 1 divided by 2");
    }

    @Test
    void executeAssignment() {
        runStatement("set x to 17");
        assertVariableEquals("x", 17);
        runStatement("set y to x squared");
        assertVariableEquals("y", 289);
    }

    @Test
    void executeBlock() {
        runProgram("set x to 17. set y to x squared.");
        assertVariableEquals("x", 17);
        assertVariableEquals("y", 289);
    }

    @Test
    void executeConditional() {
        String program =
            "if x is less than 12 then set lt to x else set lt to lt minus 1."
            + "if x equals 12 then set eq to x else set eq to eq minus 1."
            + "if x is greater than 12 then set gt to x else set gt to gt minus 1.";

        context.set("x", 11);
        runProgram(program);
        assertVariableEquals("lt", 11);
        assertVariableEquals("eq", -1);
        assertVariableEquals("gt", -1);

        context.set("x", 12);
        runProgram(program);
        assertVariableEquals("lt", 10);
        assertVariableEquals("eq", 12);
        assertVariableEquals("gt", -2);

        context.set("x", 13);
        runProgram(program);
        assertVariableEquals("lt", 9);
        assertVariableEquals("eq", 11);
        assertVariableEquals("gt", 13);
    }

    @Test
    void executeLoopExit() {
        assertThrows(LoopExited.class, () -> runStatement("exit loop"));
    }

    @Test
    void executeLoop() {
        runProgram("loop: set x to x plus 1. if x equals 10 then exit loop. set y to y plus x squared. end of loop.");
        assertVariableEquals("x", 10);
        assertVariableEquals("y", 285);
    }

    // ––––––– Helpers –––––––

    private void assertEvaluationEquals(double expected, String expression) {
        assertEquals(expected, parseExpression(expression).evaluate(context));
    }

    private void runStatement(String statement) {
        parseStatement(statement).run(context);
    }

    private void runProgram(String program) {
        parseProgram(program).run(context);
    }

    private void assertVariableEquals(String name, double expectedValue) {
        assertEquals(expectedValue, context.get(name));
    }
}
