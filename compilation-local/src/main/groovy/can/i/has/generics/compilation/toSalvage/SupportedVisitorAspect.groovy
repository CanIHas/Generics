package can.i.has.has.generics.compilation.toSalvage

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.CodeVisitorSupport
import org.codehaus.groovy.ast.GroovyCodeVisitor

import java.lang.reflect.Method

abstract class SupportedVisitorAspect extends CodeVisitorSupport{
    private GroovyCodeVisitor visitor = new CodeVisitorSupport() {}
    abstract void visitBefore(ASTNode node)
    abstract void visitAfter(ASTNode node)

    Object invokeMethod(String name, Object[] args){
        if (args.size()==1) {
            def methodToCall = CodeVisitorSupport.class.methods.find { Method m ->
                m.parameterTypes.size()==1 && name==m.name && m.parameterTypes.head().isInstance(args[0])
            }
            if (methodToCall) {
                visitBefore(args[0])
                visitor.${name}(*args)
                visitAfter(args[0])
                return
            }
        }
        return Object.metaClass.invokeMethod(this, name, args)
    }
}
