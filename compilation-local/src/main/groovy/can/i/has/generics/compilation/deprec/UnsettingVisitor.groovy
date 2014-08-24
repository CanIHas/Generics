package can.i.has.generics.compilation.deprec

import org.codehaus.groovy.ast.CodeVisitorSupport
import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression


class UnsettingVisitor extends CodeVisitorSupport{
    @Override
    void visitConstructorCallExpression(ConstructorCallExpression expression) {
        expression.metaClass = expression.metaClass.adaptee
        super.visitConstructorCallExpression(expression)
    }

    @Override
    void visitCastExpression(CastExpression expression){
        expression.metaClass = expression.metaClass.adaptee
        super.visitCastExpression(expression)
    }
}
