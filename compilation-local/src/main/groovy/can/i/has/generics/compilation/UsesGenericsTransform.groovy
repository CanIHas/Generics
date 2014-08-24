package can.i.has.has.generics.compilation

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
class UsesGenericsTransform extends AbstractASTTransformation {

    static final CodeTransformer transformer = new CodeTransformer(
        new GenericsWrappingTransformation(
            CompilePhase.SEMANTIC_ANALYSIS
        )
    )

    static BlockStatement transformBlock(BlockStatement block){
        return new BlockStatement(block.statements.collect {
            transformer.transform(it, CompilePhase.SEMANTIC_ANALYSIS)
        }, block.variableScope)
    }

    static void transformMethodInPlace(MethodNode node){
        node.code = transformBlock(node.code)
    }

    @Override
    void visit(ASTNode[] astNodes, SourceUnit source) {
        if (!astNodes) return
        if (!astNodes[0]) return
        if (!astNodes[1]) return
        if (!(astNodes[0] instanceof AnnotationNode)) return
        if (astNodes[0].classNode?.name != UsesGenerics.name) return
        switch (astNodes[1]) {
            case MethodNode: transformMethodInPlace(astNodes[1]); break
            case ClassNode: (astNodes[1] as ClassNode).methods.each { transformMethodInPlace(it) }; break
            default: break
        }
    }
}
