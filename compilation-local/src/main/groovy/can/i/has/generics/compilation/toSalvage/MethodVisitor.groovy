package can.i.has.has.generics.compilation.toSalvage

import can.i.has.has.generics.compilation.deprec.ASTCategory
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit


class MethodVisitor {
    void visit(MethodNode method, SourceUnit source) {
        println "Visiting METHOD ${method.name}"
        println "method: $method"
        println "source: $source"
        def visitor = new VisitorImpl()
        def builder = new AstBuilder()

        ASTCategory.withExtendedText(method.code) {

            method.code = new BlockStatement(
                (method.code as BlockStatement).statements.collect {
                    builder.buildFromString(CompilePhase.SEMANTIC_ANALYSIS, true, it.getText())
                }.flatten(),
                (method.code as BlockStatement).variableScope
            )
        }
//        method.code.eachWithIndex { Statement s, int i ->
//            println "statement #$i: $s"
//            println "class ${s.class}"
//            s.visitNode(visitor)
//        }
    }

}
