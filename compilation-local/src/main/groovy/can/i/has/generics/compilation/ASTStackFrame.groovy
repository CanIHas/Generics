package can.i.has.generics.compilation

import org.codehaus.groovy.ast.ASTNode

import groovy.transform.Canonical

@Canonical
class ASTStackFrame {
    @Canonical
    static final class Pair<T1, T2> {
        final T1 first
        final T2 second
    }
    ASTNode transformedNode
    boolean recursive = true
    List<Pair<String, String>> replacements = []
    ASTNode transformationResult = null

    void addReplacement(original, replacement){
        if (original instanceof ASTNode)
            original = original.text
        if (replacement instanceof ASTNode)
            replacement = replacement.text
        assert original instanceof String
        assert replacement instanceof String
        replacements.add new Pair<String, String>(original, replacement)
    }
}
