package can.i.has.has.generics.compilation

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase

import groovy.transform.Canonical

@Canonical
class CodeTransformer{
    CodeTransformation transformation

    private List<ASTStackFrame> stack = []

    synchronized ASTNode transform(ASTNode node, CompilePhase phase){
        assert stack.isEmpty()
        stack.add(new ASTStackFrame())
        node.visit(new TransformingVisitor(phase))
        return introduceChanges(node, stack.pop(), phase)
    }

    @Canonical
    private class TransformingVisitor extends OneMethodVisitor {
        CompilePhase phase

        @Override
        void visitNode(ASTNode node) {
            def frame = new ASTStackFrame(node)
            stack.add(frame)
            def transformed = transformation.transform(node, frame)
            if (!frame.recursive)
                throwToBreakRecursion()
            frame.transformationResult = transformed
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
        if (context.replacements.empty)
            return node
        String txt = node.text
        context.replacements.each { ASTStackFrame.Pair<String, String> pair ->
            txt = txt.replaceFirst(regexed(pair.first), pair.second)
        }
        def out = new AstBuilder().buildFromString(phase, true, txt)
        assert out.size() == 1
        out = out[0]
        if (node instanceof Expression) {
            assert out instanceof ExpressionStatement
            return out.expression
        }
        assert node instanceof Statement
        assert out instanceof Statement
        return out

    }

    private static String regexed(String str){
        str.collect { "[$it]" }.join("")
    }

    void reset() {
        stack = []
    }
}
