package can.i.has.generics.compilation

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.control.CompilePhase

import groovy.transform.Canonical
import groovy.util.logging.Slf4j

@Canonical
@Slf4j
class GenericsWrappingTransformation implements CodeTransformation{
    CompilePhase phase

    @Override
    ASTNode transform(ASTNode node, ASTStackFrame context) {
        log.debug "-- GenericsWrappingTransform.transform --"
        log.debug "got node $node\n"
        log.debug "with text:\n"
        log.debug node.text+"\n"
        def out
        switch (node) {
            case CastExpression:
            case ConstructorCallExpression:
                out = ASTUtils.parseExpression(
                        phase,
                        "can.i.has.generics.runtime.GenericsRuntime.makeMeGeneric(${node.text}, E: Object)"
                    )
                break
            default:
                out = node
        }
        log.debug "returning $out\n"
        log.debug "with text:\n"
        log.debug out.text+"\n"
        log.debug "---------------------------------------------"
        return out
    }
}
