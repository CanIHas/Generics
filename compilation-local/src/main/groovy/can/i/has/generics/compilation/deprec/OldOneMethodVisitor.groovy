package can.i.has.generics.compilation.deprec

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.CodeVisitorSupport

import groovy.transform.Canonical
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j

import java.lang.reflect.Method

@Slf4j
abstract class OldOneMethodVisitor extends CodeVisitorSupport{
    @InheritConstructors static final class NotRecursiveTraversalException extends RuntimeException {}

    abstract void visitNode(ASTNode node)
    abstract void finalizeNode(ASTNode node)

    static {
        MetaClassRegistry registry = GroovySystem.metaClassRegistry
        def aspect = new OneMethodVisitorMetaClass(registry, this.class)
        registry.setMetaClass(OldOneMethodVisitor, aspect)
    }

//    void modifyMetaClass(){
//        MetaClassRegistry registry = GroovySystem.metaClassRegistry
//        if (!(registry.getMetaClass(this.class) instanceof OneMethodVisitorMetaClass)) {
//            def aspect = new OneMethodVisitorMetaClass(registry, this.class)
//            registry.setMetaClass(this.class, aspect)
//        }
//    }


    @Canonical
    private static class OneMethodVisitorMetaClass extends MetaClassImpl implements AdaptingMetaClass {
        MetaClass adaptee

        OneMethodVisitorMetaClass(Class theClass, MetaMethod[] add) {
            super(theClass, add)
        }

        OneMethodVisitorMetaClass(Class theClass) {
            super(theClass)
        }

        OneMethodVisitorMetaClass(MetaClassRegistry registry, Class theClass, MetaMethod[] add) {
            super(registry, theClass, add)
        }

        OneMethodVisitorMetaClass(MetaClassRegistry registry, Class theClass) {
            super(registry, theClass)
        }

        @Override
        Object invokeMethod(Class sender, Object receiver, String name, Object[] args, boolean isCallToSuper, boolean fromInsideClass) {
            log.debug "Invoking $name with $args"
            if (args.size()==1) {
                def methodToCall = CodeVisitorSupport.class.methods.find { Method m ->
                    m.parameterTypes.size()==1 && name==m.name && m.parameterTypes.head().isInstance(args[0])
                }
                if (methodToCall) {
                    log.debug "Invoking visitNode()"
                    try {
                        receiver.visitNode(args[0])
                        log.debug "Traversing children of ${args[0]}"
                        adaptee.invokeMethod(sender, receiver, name, args, isCallToSuper, fromInsideClass)
                    } catch (NotRecursiveTraversalException ignored) {
                        log.debug "Not traversing children of ${args[0]}"
                    }
                    receiver.finalizeNode(args[0])
                    return
                }
            }
            log.debug "Invoking as Object"
            return adaptee.invokeMethod(sender, receiver, name, args, isCallToSuper, fromInsideClass)
        }
    }

//    Object invokeMethod(String name, Object[] args){
//        log.debug "Invoking $name with $args"
//        if (args.size()==1) {
//            def methodToCall = CodeVisitorSupport.class.methods.find { Method m ->
//                m.parameterTypes.size()==1 && name==m.name && m.parameterTypes.head().isInstance(args[0])
//            }
//            if (methodToCall) {
//                log.debug "Invoking visitNode()"
//                try {
//                    visitNode(args[0])
//                    log.debug "Traversing children of ${args[0]}"
//                    super."$methodToCall"(*args)
//                } catch (NotRecursiveTraversalException ignored) {
//                    log.debug "Not traversing children of ${args[0]}"
//                }
//                finalizeNode(args[0])
//                return
//            }
//        }
//        log.debug "Invoking as Object"
//        return Object.metaClass.invokeMethod(this, name, args)
//    }

    void throwToBreakRecursion(String msg = "This is raised to change control flow."){
        throw new NotRecursiveTraversalException(msg)
    }
}
