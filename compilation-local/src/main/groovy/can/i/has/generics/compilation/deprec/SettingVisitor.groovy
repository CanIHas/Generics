package can.i.has.has.generics.compilation.deprec

import can.i.has.has.generics.compilation.deprec.ASTInterceptor
import org.codehaus.groovy.ast.CodeVisitorSupport
import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression

import groovy.transform.Canonical

@Canonical
class SettingVisitor extends CodeVisitorSupport{
    static final MetaClassRegistry registry = GroovySystem.getMetaClassRegistry()
    static final Interceptor interceptor = new ASTInterceptor()

    @Override
    void visitConstructorCallExpression(ConstructorCallExpression expression) {
        println "setting in $expression"
        def meta = new ProxyMetaClass(registry, expression.class, expression.metaClass)
        meta.interceptor = interceptor
        expression.metaClass = meta
        super.visitConstructorCallExpression(expression)
    }

    @Override
    void visitCastExpression(CastExpression expression){
        println "setting in $expression"
        def meta = new ProxyMetaClass(registry, expression.class, expression.metaClass)
        meta.interceptor = interceptor
        expression.metaClass = meta
        super.visitCastExpression(expression)
    }
}
