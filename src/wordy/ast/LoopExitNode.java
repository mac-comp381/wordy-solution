package wordy.ast;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

import wordy.interpreter.EvaluationContext;
import wordy.interpreter.LoopExited;

/**
 * A statement that causes program flow to exit the nearest-nested loop. Often called “break” in
 * other languages.
 * 
 * The interpreter implements this by throwing a `LoopExited` exception.
 */
public final class LoopExitNode extends StatementNode {
    public LoopExitNode() {
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return Collections.emptyMap();
    }

    @Override
    public void doRun(EvaluationContext context) {
        throw new LoopExited();
    }

    @Override
    public void compile(PrintWriter out) {
        out.println("break;");
    }

    @Override
    public boolean equals(Object o) {
        return this == o
            || o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "LoopExitNode";
    }
}
