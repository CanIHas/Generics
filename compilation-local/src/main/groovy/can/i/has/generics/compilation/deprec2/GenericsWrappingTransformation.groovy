package can.i.has.generics.compilation.deprec2

import can.i.has.generics.compilation.ASTUtils
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.GenericsType
import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.control.CompilePhase

import groovy.transform.Canonical
import groovy.util.logging.Slf4j

@Canonical
@Slf4j
class GenericsWrappingTransformation implements CodeTransformation{
    CompilePhase phase

    String genericParametersMapping(CastExpression expression){
        def map = [:]
        def realTypes = expression.type.genericsTypes.collect { (it as GenericsType).type.typeClass.name }
        def symbolicTypes = expression.type.redirect.genericsTypes.collect {it.name }
        symbolicTypes.eachWithIndex { String entry, int i ->
            map[entry] = realTypes[i]
        }
        def mapstr = "[${map.collect{k, v -> "$k : Class.forName('$v')"}.join(", ")}] as Map"
        return mapstr
    }

    String genericParametersMapping(ConstructorCallExpression expression){
        "[:]"
    }

    @Override
    ASTNode transform(ASTNode node, ASTStackFrame context) {
        log.debug "-- GenericsWrappingTransform.transform --"
        log.debug "got node $node"
        log.debug "with text:"
        log.debug node.text
        def out
        switch (node) {
            case CastExpression:
            case ConstructorCallExpression:
                def mapping = genericParametersMapping(node)
                out = (
                    mapping == null ? node : ASTUtils.parseExpression(
                            phase,
                            "can.i.has.generics.runtime.GenericsRuntime." +
                                "makeMeGeneric(${mapping}, ${node.text})"
                        )
                )
                break
            default:
                out = node
        }
        log.debug "returning $out"
        log.debug "with text:"
        log.debug out.text
        log.debug "---------------------------------------------"
        return out
    }
}
