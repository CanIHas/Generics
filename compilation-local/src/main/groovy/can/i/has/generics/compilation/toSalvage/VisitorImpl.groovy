package can.i.has.has.generics.compilation.toSalvage

import can.i.has.has.generics.runtime.GenericsRuntime
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.CodeVisitorSupport
import org.codehaus.groovy.ast.GenericsType
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.ExpressionTransformer
import org.codehaus.groovy.ast.expr.MapEntryExpression
import org.codehaus.groovy.ast.expr.MapExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement

class VisitorImpl extends CodeVisitorSupport {
    @Override
    void visitExpressionStatement(ExpressionStatement statement) {
        println "expression $statement"
        super.visitExpressionStatement(statement)
    }

    void visitCastExpression(CastExpression expression){
        if (expression.expression.type.genericsTypes.size()!=0) {
            println "HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
            def map = [:]
            def realTypes = expression.type.genericsTypes.collect { (it as GenericsType).type.typeClass.name }
            def symbolicTypes = expression.type.redirect.genericsTypes.collect {it.name }
            symbolicTypes.eachWithIndex { String entry, int i ->
                map[entry] = realTypes[i]
            }
            def mapstr = "[${map.collect{k, v -> "$k : Class.forNode('$v')"}.join(", ")}] as Map"
            println mapstr
            println new AstBuilder().buildFromString(mapstr).statements[0][0].text
            println new AstBuilder().buildFromString(mapstr).statements[0][0].expression
            println '------------------'
            println expression.transformExpression(new ExpressionTransformer() {
                def
                @Override
                Expression transform(Expression expr) {
                    new MethodCallExpression(
                        new ClassExpression(
                            new ClassNode(
                                GenericsRuntime
                            )
                        ),
                        "makeMeGeneric",
                        new ArgumentListExpression(
                            [
                                new AstBuilder().buildFromString(mapstr),
                                expr
                            ]
                        )
                    )
                }
            })
        }
        println "cast $expression"
        super.visitCastExpression(expression)
        println expression.type
        println expression.type.genericsTypes.collect { (it as GenericsType).type.typeClass.name }
        println expression.type.redirect.genericsTypes.collect {it.name}
        println expression.type.redirect.genericsTypes.collect { (it as GenericsType).type.typeClass.name }
    }

    @Override
    void visitConstructorCallExpression(ConstructorCallExpression expression) {
        println "constructor $expression"
        super.visitConstructorCallExpression(expression)
    }

}
