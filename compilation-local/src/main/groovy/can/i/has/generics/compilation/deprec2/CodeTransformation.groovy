package can.i.has.generics.compilation.deprec2

import org.codehaus.groovy.ast.ASTNode


public interface CodeTransformation {
    ASTNode transform(ASTNode node, ASTStackFrame context)
}