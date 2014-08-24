package can.i.has.has.generics.compilation.toSalvage

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.CodeVisitorSupport

import groovy.transform.Canonical

@Canonical
class TransformationContext {
    final ASTNode parent

    boolean recursive = true
    // list of pairs of strings
    List<List<String>> changes = []

    protected ASTNode currentNode


    class Visitor extends CodeVisitorSupport {

    }
}
