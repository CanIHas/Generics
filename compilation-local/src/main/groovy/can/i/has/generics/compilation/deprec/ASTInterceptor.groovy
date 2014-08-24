package can.i.has.generics.compilation.deprec

import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression


class ASTInterceptor implements Interceptor{
    @Override
    Object beforeInvoke(Object object, String methodName, Object[] arguments) {
        println "before $object $methodName"
        1
    }

    @Override
    Object afterInvoke(Object object, String methodName, Object[] arguments, Object result) {
        println "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX $object $methodName $arguments"
        if (object instanceof CastExpression && "getText"==methodName) {
            println "cast $object"
            return result
        } else if (object instanceof ConstructorCallExpression && "getText"==methodName) {
            println "constructor $object"
            return result
        }
        return result
    }

    @Override
    boolean doInvoke() {
        println "doInvoke"
        true
    }
}
