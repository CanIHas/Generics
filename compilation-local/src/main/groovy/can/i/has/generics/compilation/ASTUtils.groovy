package can.i.has.generics.compilation

import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase


class ASTUtils {
    protected static final AstBuilder astBuilder = new AstBuilder()

    static Statement parseStatement(CompilePhase phase, String statementCode){
        astBuilder.buildFromString(phase, true, statementCode)[0].statements[0]
    }

    static Expression parseExpression(CompilePhase phase, String expressionCode){
        parseStatement(phase, expressionCode).expression
    }
}
