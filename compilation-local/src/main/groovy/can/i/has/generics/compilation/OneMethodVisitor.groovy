package can.i.has.generics.compilation

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.CodeVisitorSupport
import org.codehaus.groovy.ast.GroovyCodeVisitor
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ArrayExpression
import org.codehaus.groovy.ast.expr.AttributeExpression
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.BitwiseNegationExpression
import org.codehaus.groovy.ast.expr.BooleanExpression
import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.ClosureListExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codehaus.groovy.ast.expr.ElvisOperatorExpression
import org.codehaus.groovy.ast.expr.FieldExpression
import org.codehaus.groovy.ast.expr.GStringExpression
import org.codehaus.groovy.ast.expr.ListExpression
import org.codehaus.groovy.ast.expr.MapEntryExpression
import org.codehaus.groovy.ast.expr.MapExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.MethodPointerExpression
import org.codehaus.groovy.ast.expr.NotExpression
import org.codehaus.groovy.ast.expr.PostfixExpression
import org.codehaus.groovy.ast.expr.PrefixExpression
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.expr.RangeExpression
import org.codehaus.groovy.ast.expr.SpreadExpression
import org.codehaus.groovy.ast.expr.SpreadMapExpression
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression
import org.codehaus.groovy.ast.expr.TernaryExpression
import org.codehaus.groovy.ast.expr.TupleExpression
import org.codehaus.groovy.ast.expr.UnaryMinusExpression
import org.codehaus.groovy.ast.expr.UnaryPlusExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.AssertStatement
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.BreakStatement
import org.codehaus.groovy.ast.stmt.CaseStatement
import org.codehaus.groovy.ast.stmt.CatchStatement
import org.codehaus.groovy.ast.stmt.ContinueStatement
import org.codehaus.groovy.ast.stmt.DoWhileStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.ForStatement
import org.codehaus.groovy.ast.stmt.IfStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.ast.stmt.SwitchStatement
import org.codehaus.groovy.ast.stmt.SynchronizedStatement
import org.codehaus.groovy.ast.stmt.ThrowStatement
import org.codehaus.groovy.ast.stmt.TryCatchStatement
import org.codehaus.groovy.ast.stmt.WhileStatement
import org.codehaus.groovy.classgen.BytecodeExpression

import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j

import java.lang.reflect.Method

@Slf4j
abstract class OneMethodVisitor extends CodeVisitorSupport{
    @InheritConstructors static final class NotRecursiveTraversalException extends RuntimeException {}

    abstract void visitNode(ASTNode node)
    abstract void finalizeNode(ASTNode node)

    void throwToBreakRecursion(String msg = "This is raised to change control flow."){
        throw new NotRecursiveTraversalException(msg)
    }

    void doSomething(String name, ASTNode node){
        log.debug "Invoking $name with $node"
        log.debug "Invoking visitNode()"
        try {
            visitNode(node)
            log.debug "Traversing children of ${node}"
            super."$name"(node)
        } catch (NotRecursiveTraversalException ignored) {
            log.debug "Not traversing children of ${node}"
        }
        finalizeNode(node)
    }

    @Override
    void visitBlockStatement(BlockStatement statement) {
        doSomething("visitBlockStatement", statement)
    }

    @Override
    void visitForLoop(ForStatement forLoop) {
        doSomething("visitForLoop", forLoop)
    }

    @Override
    void visitWhileLoop(WhileStatement loop) {
        doSomething("visitWhileLoop", loop)
    }

    @Override
    void visitDoWhileLoop(DoWhileStatement loop) {
        doSomething("visitDoWhileLoop", loop)
    }

    @Override
    void visitIfElse(IfStatement ifElse) {
        doSomething("visitIfElse", ifElse)
    }

    @Override
    void visitExpressionStatement(ExpressionStatement statement) {
        doSomething("visitExpressionStatement", statement)
    }

    @Override
    void visitReturnStatement(ReturnStatement statement) {
        doSomething("visitReturnStatement", statement)
    }

    @Override
    void visitAssertStatement(AssertStatement statement) {
        doSomething("visitAssertStatement", statement)
    }

    @Override
    void visitTryCatchFinally(TryCatchStatement finally1) {
        doSomething("visitTryCatchFinally", finally1)
    }

    @Override
    void visitSwitch(SwitchStatement statement) {
        doSomething("visitSwitch", statement)
    }

    @Override
    void visitCaseStatement(CaseStatement statement) {
        doSomething("visitCaseStatement", statement)
    }

    @Override
    void visitBreakStatement(BreakStatement statement) {
        doSomething("visitBreakStatement", statement)
    }

    @Override
    void visitContinueStatement(ContinueStatement statement) {
        doSomething("visitContinueStatement", statement)
    }

    @Override
    void visitThrowStatement(ThrowStatement statement) {
        doSomething("visitThrowStatement", statement)
    }

    @Override
    void visitSynchronizedStatement(SynchronizedStatement statement) {
        doSomething("visitSynchronizedStatement", statement)
    }

    @Override
    void visitCatchStatement(CatchStatement statement) {
        doSomething("visitCatchStatement", statement)
    }

    @Override
    void visitMethodCallExpression(MethodCallExpression call) {
        doSomething("visitMethodCallExpression", call)
    }

    @Override
    void visitStaticMethodCallExpression(StaticMethodCallExpression expression) {
        doSomething("visitStaticMethodCallExpression", expression)
    }

    @Override
    void visitConstructorCallExpression(ConstructorCallExpression expression) {
        doSomething("visitConstructorCallExpression", expression)
    }

    @Override
    void visitTernaryExpression(TernaryExpression expression) {
        doSomething("visitTernaryExpression", expression)
    }

    @Override
    void visitShortTernaryExpression(ElvisOperatorExpression expression) {
        doSomething("visitShortTernaryExpression", expression)
    }

    @Override
    void visitBinaryExpression(BinaryExpression expression) {
        doSomething("visitBinaryExpression", expression)
    }

    @Override
    void visitPrefixExpression(PrefixExpression expression) {
        doSomething("visitPrefixExpression", expression)
    }

    @Override
    void visitPostfixExpression(PostfixExpression expression) {
        doSomething("visitPostfixExpression", expression)
    }

    @Override
    void visitBooleanExpression(BooleanExpression expression) {
        doSomething("visitBooleanExpression", expression)
    }

    @Override
    void visitClosureExpression(ClosureExpression expression) {
        doSomething("visitClosureExpression", expression)
    }

    @Override
    void visitTupleExpression(TupleExpression expression) {
        doSomething("visitTupleExpression", expression)
    }

    @Override
    void visitMapExpression(MapExpression expression) {
        doSomething("visitMapExpression", expression)
    }

    @Override
    void visitMapEntryExpression(MapEntryExpression expression) {
        doSomething("visitMapEntryExpression", expression)
    }

    @Override
    void visitListExpression(ListExpression expression) {
        doSomething("visitListExpression", expression)
    }

    @Override
    void visitRangeExpression(RangeExpression expression) {
        doSomething("visitRangeExpression", expression)
    }

    @Override
    void visitPropertyExpression(PropertyExpression expression) {
        doSomething("visitPropertyExpression", expression)
    }

    @Override
    void visitAttributeExpression(AttributeExpression attributeExpression) {
        doSomething("visitAttributeExpression", attributeExpression)
    }

    @Override
    void visitFieldExpression(FieldExpression expression) {
        doSomething("visitFieldExpression", expression)
    }

    @Override
    void visitMethodPointerExpression(MethodPointerExpression expression) {
        doSomething("visitMethodPointerExpression", expression)
    }

    @Override
    void visitConstantExpression(ConstantExpression expression) {
        doSomething("visitMethodPointerExpression", expression)
    }

    @Override
    void visitClassExpression(ClassExpression expression) {
        doSomething("visitClassExpression", expression)
    }

    @Override
    void visitVariableExpression(VariableExpression expression) {
        doSomething("visitVariableExpression", expression)
    }

    @Override
    void visitDeclarationExpression(DeclarationExpression expression) {
        doSomething("visitDeclarationExpression", expression)
    }

    @Override
    void visitGStringExpression(GStringExpression expression) {
        doSomething("visitGStringExpression", expression)
    }

    @Override
    void visitArrayExpression(ArrayExpression expression) {
        doSomething("visitArrayExpression", expression)
    }

    @Override
    void visitSpreadExpression(SpreadExpression expression) {
        doSomething("visitSpreadExpression", expression)
    }

    @Override
    void visitSpreadMapExpression(SpreadMapExpression expression) {
        doSomething("visitSpreadMapExpression", expression)
    }

    @Override
    void visitNotExpression(NotExpression expression) {
        doSomething("visitNotExpression", expression)
    }

    @Override
    void visitUnaryMinusExpression(UnaryMinusExpression expression) {
        doSomething("visitUnaryMinusExpression", expression)
    }

    @Override
    void visitUnaryPlusExpression(UnaryPlusExpression expression) {
        doSomething("visitUnaryPlusExpression", expression)
    }

    @Override
    void visitBitwiseNegationExpression(BitwiseNegationExpression expression) {
        doSomething("visitBitwiseNegationExpression", expression)
    }

    @Override
    void visitCastExpression(CastExpression expression) {
        doSomething("visitCastExpression", expression)
    }

    @Override
    void visitArgumentlistExpression(ArgumentListExpression expression) {
        doSomething("visitArgumentlistExpression", expression)
    }

    @Override
    void visitClosureListExpression(ClosureListExpression closureListExpression) {
        doSomething("visitClosureListExpression", closureListExpression)
    }

    @Override
    void visitBytecodeExpression(BytecodeExpression expression) {
        doSomething("visitBytecodeExpression", expression)
    }
}
