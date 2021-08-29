package wordy.ast;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A node in the abstract syntax tree of a parser Wordy program.
 */
public abstract class ASTNode {
    /**
     * Returns all the children of this node (immediate children, not all descendants).
     * @return A map whose values are child nodes, and whose keys are human-readable labels that
     *         uniquely identify each child node’s role.
     */
    public abstract Map<String, ASTNode> getChildren();

    /**
     * Translates this node and its descendants into Java code.
     */
    public abstract void compile(PrintWriter out);

    /**
     * Returns the set of all unique (by name) VariableNodes in this node's subtree. The results
     * include this node itself if it is a VariableNode.
     */
    public Set<VariableNode> findAllVariables() {
        Set<VariableNode> results = new HashSet<>();
        forEachVariable(results::add);
        return results;
    }

    private void forEachVariable(Consumer<VariableNode> consumer) {
        if(this instanceof VariableNode)
            consumer.accept((VariableNode) this);
        for(var child : getChildren().values()) {
            child.forEachVariable(consumer);
        }
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    /**
     * Generates a multiline string indicating the tree structure of this node and its descendants.
     */
    public String dump() {
        return dump(new StringBuffer(), null, "", true).toString();
    }

    private StringBuffer dump(StringBuffer out, String label, String indent, boolean lastChild) {
        out.append(indent);

        if(label != null) {  // null label means root node
            // Draw tree lines
            out.append(lastChild ? "└─" : "├─");
            indent += lastChild ? "  " : "│ ";

            // Identify this node with the label from its parent’s getChildren() method
            out.append(label);
            out.append(": ");

            // Indent children past label
            indent += " ".repeat(label.length() + 2);
        }

        out.append(getClass().getSimpleName());
        out.append(' ');
        out.append(describeAttributes());
        out.append('\n');

        for(var childIter = getChildren().entrySet().iterator(); childIter.hasNext(); ) {
            var entry = childIter.next();
            entry.getValue().dump(out, entry.getKey(), indent, !childIter.hasNext());
        }
        return out;
    }

    /**
     * Describes the properties of this node, such as a variable’s name or a constant’s value.
     */
    protected String describeAttributes() {
        return "";
    }
}
