package can.i.has.has.generics.compilation

import org.codehaus.groovy.ast.ASTNode


public interface CodeTransformation {
    ASTNode transform(ASTNode node, ASTStackFrame context)
}