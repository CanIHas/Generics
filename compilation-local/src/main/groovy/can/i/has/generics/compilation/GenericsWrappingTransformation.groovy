package can.i.has.has.generics.compilation

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.control.CompilePhase

import groovy.transform.Canonical

@Canonical
class GenericsWrappingTransformation implements CodeTransformation{
    CompilePhase phase
    final static AstBuilder astBuilder = new AstBuilder()

    @Override
    ASTNode transform(ASTNode node, ASTStackFrame context) {
        switch (node) {
            case CastExpression:
            case ConstructorCallExpression:
                return (
                    astBuilder.buildFromString(
                        phase,
                        true,
                        "can.i.has.generics.runtime.GenericsRuntime.makeMeGeneric(${node.text}, E: Object)"
                    ) as ExpressionStatement
                ).expression
            default: return node
        }
    }
}
