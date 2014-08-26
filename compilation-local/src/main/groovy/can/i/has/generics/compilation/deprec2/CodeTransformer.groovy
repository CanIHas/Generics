package can.i.has.generics.compilation.deprec2

import can.i.has.generics.compilation.ASTUtils
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase

import groovy.transform.Canonical
import groovy.util.logging.Slf4j

@Canonical
@Slf4j
class CodeTransformer{
    CodeTransformation transformation

    private List<ASTStackFrame> stack = []

    synchronized ASTNode transform(ASTNode node, CompilePhase phase){
        log.debug "transform($node)"
        assert stack.isEmpty()
        stack.add(new ASTStackFrame())
        log.debug "should visit now"
        node.visit(new TransformingVisitor(phase))
        log.debug "just visited"
        def out = introduceChanges(node, stack.pop(), phase)
        log.debug "transform returned $out"
        return out
    }

    @Canonical
    private class TransformingVisitor extends OneMethodVisitor {
        CompilePhase phase

        @Override
        void visitNode(ASTNode node) {
            log.debug "TransformingVisitor#visitNode($node)"
            def frame = new ASTStackFrame(node)
            stack.add(frame)
            def transformed = transformation.transform(node, frame)
            log.debug "TransformingVisitor#visitNode recursive? ${frame.recursive}"
            frame.transformationResult = transformed
            if (!frame.recursive)
                throwToBreakRecursion()
        }

        @Override
        void finalizeNode(ASTNode node) {
            def frame = stack.pop()
            def transformed = frame.transformationResult
            transformed = introduceChanges(transformed, frame, phase)
            if (!transformed.is(node))
                stack.last().addReplacement(node.text, transformed.text)
        }
    }

    protected static ASTNode introduceChanges(ASTNode node, ASTStackFrame context, CompilePhase phase){
        log.debug "introducing changes to $node"
        log.debug "in context: $context"
        if (context.replacements.empty)
            return node
        String txt = node.text
        log.debug "text: $txt"
        context.replacements.each { ASTStackFrame.Pair<String, String> pair ->
            log.debug "replacing: $pair / ${regexed(pair.first)}"
            txt = txt.replaceFirst(regexed(pair.first), pair.second)
        }
        def out
        if (node instanceof Expression)
            out = ASTUtils.parseExpression(phase, txt)
        else if (node instanceof Statement)
            out = ASTUtils.parseStatement(phase, txt)
        else assert false//todo: exception
        log.debug "with result $out"
        log.debug "with text:"
        log.debug "${out.text}"
        return out

    }

    private static String regexed(String str){
        str.collect { "[$it]" }.join("")
    }

    void reset() {
        stack = []
    }
}
