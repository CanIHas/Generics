package can.i.has.has.generics.compilation

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.CodeVisitorSupport

import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j

import java.lang.reflect.Method

@Slf4j
abstract class OneMethodVisitor extends CodeVisitorSupport{
    @InheritConstructors static final class NotRecursiveTraversalException extends RuntimeException {}

    abstract void visitNode(ASTNode node)
    abstract void finalizeNode(ASTNode node)

    Object invokeMethod(String name, Object[] args){
        log.debug "Invoking $name with $args"
        if (args.size()==1) {
            def methodToCall = CodeVisitorSupport.class.methods.find { Method m ->
                m.parameterTypes.size()==1 && name==m.name && m.parameterTypes.head().isInstance(args[0])
            }
            if (methodToCall) {
                log.debug "Invoking visitNode()"
                try {
                    visitNode(args[0])
                    log.debug "Traversing children of ${args[0]}"
                    super."$methodToCall"(*args)
                } catch (NotRecursiveTraversalException ignored) {
                    log.debug "Not traversing children of ${args[0]}"
                }
                finalizeNode(args[0])
                return
            }
        }
        log.debug "Invoking as Object"
        return Object.metaClass.invokeMethod(this, name, args)
    }

    void throwToBreakRecursion(String msg = "This is raised to change control flow."){
        throw new NotRecursiveTraversalException(msg)
    }
}
