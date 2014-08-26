package can.i.has.generics.compilation

import can.i.has.generics.runtime.GenericsRuntime
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.GenericsType
import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit

import static org.codehaus.groovy.ast.tools.GeneralUtils.*
import static can.i.has.generics.compilation.ASTUtils.*

import groovy.transform.Canonical

@Canonical
class GenericsWrappingExpressionTransformer extends ClassCodeExpressionTransformer{
    SourceUnit sourceUnit
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
    Expression transform(Expression node){
        def out
        switch (node) {
            case CastExpression:
            case ConstructorCallExpression:
                def mapping = genericParametersMapping(node)
                out = (
                mapping == null ? node :
                    callX(
                        new ClassNode(GenericsRuntime),
                        "makeMeGeneric",
                        args(
                            new CastExpression(
                                new ClassNode(Map),
                                parseExpression(
                                    phase,
                                    mapping
                                )
                            ),
                            new CastExpression(
                                new ClassNode(Object),
                                node
                            )
                        )
                    )
                /*
                ASTUtils.parseExpression(
                    PHASE,
                    "can.i.has.generics.runtime.GenericsRuntime." +
                        "makeMeGeneric(${mapping}, ${node.text})"
                )
                 */
                )
                break
            default:
                out = node
        }
        return out
    }
}
