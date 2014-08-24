package can.i.has.generics.gathering

import can.i.has.generics.model.GenericClassDescriptor
import can.i.has.generics.model.GenericMethodDescriptor
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.GenericsType
import org.codehaus.groovy.ast.InnerClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase=CompilePhase.CONVERSION)
class GenericsGatheringTransform implements ASTTransformation{
    GenericDescriptorSaver saver = new GenericDescriptorSaver()

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        try {
            ModuleNode module = nodes[0]
            saver = new GenericDescriptorSaver(new File("./genericsDesc"))
            saver.openBatch()
            module.classes.findAll { ClassNode classNode ->
                classNode.outerClass == null
            }.each {
                processClass(it)
            }
            saver.closeBatch()
        } catch (Throwable t){
            source.addException(t)
        }
    }

    void processClass(ClassNode classNode) {
        if (classNode.usingGenerics) {
            def methodsDesc = classNode.methods.collect{ extractDescriptor(classNode.genericsTypes.toList(), it) }.findAll()
            def classDesc = new GenericClassDescriptor(
                dottedName(classNode.nameWithoutPackage),
                dottedName(classNode.name),
                methodsDesc,
                classNode.genericsTypes.collect {it.name},
                [:]
            )
            saver.addClass classDesc
        }
        classNode.innerClasses.each { InnerClassNode node ->
            processClass(node)
        }
    }

    GenericMethodDescriptor extractDescriptor(List<GenericsType> generics, MethodNode methodNode){
        def idxs = findIdxs(generics, methodNode)
        idxs.isEmpty() ? null : new GenericMethodDescriptor(
            methodNode.name,
            methodNode.parameters.collect { Parameter p -> p.type.name }
        )
    }

    Map<String, List<Integer>> findIdxs(List<GenericsType> generics, MethodNode methodNode){
        def out = [:].withDefault { key -> [] }
        methodNode.parameters.eachWithIndex { Parameter entry, int i ->
            if (generics.any { it.name == entry.type.name })
                out[entry.type.name].add i
        }
        return out
    }

    String dottedName(String name){
        name.replaceAll(/[$]/, ".")
    }

}
