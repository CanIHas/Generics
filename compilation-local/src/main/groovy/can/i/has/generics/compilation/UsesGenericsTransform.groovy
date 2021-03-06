package can.i.has.generics.compilation

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
class UsesGenericsTransform extends AbstractASTTransformation {

    @Override
    void visit(ASTNode[] astNodes, SourceUnit source) {
        if (!astNodes) return
        if (!astNodes[0]) return
        if (!astNodes[1]) return
        if (!(astNodes[0] instanceof AnnotationNode)) return
        if (astNodes[0].classNode?.name != UsesGenerics.name) return
        def transfomer = new GenericsWrappingExpressionTransformer(source, CompilePhase.SEMANTIC_ANALYSIS)

        switch (astNodes[1]) {
            case MethodNode: transfomer.visitMethod(astNodes[1] as MethodNode); break
            case ClassNode: transfomer.visitClass(astNodes[1] as ClassNode); break
            default: break
        }
    }
}
