package can.i.has.generics.compilation.deprec

import org.codehaus.groovy.ast.ASTNode

class ASTCategory {
    static boolean used = false

    static <T> T withExtendedText(ASTNode node, Closure<T> c){
        node.visit(new SettingVisitor())
        try {
            c()
        } finally {
            node.visit(new UnsettingVisitor())
        }

    }

    /*
    def oldCastMeta = CastExpression.metaClass
        CastExpression.metaClass.getText = { CastExpression expression ->
            def returned = oldCastMeta.invokeMethod(expression, "getText", [] as Object[])
            if (expression.expression.type.genericsTypes.size()!=0) {
                used = true
                println "GenericsRuntime.makeMeGeneric(<MAPA>, ${returned}"
            }
            returned
        }
        def oldConstructorMeta = ConstructorCallExpression.metaClass
        ConstructorCallExpression.metaClass.getText = { ConstructorCallExpression expression ->
            def returned = oldConstructorMeta.invokeMethod(expression, "getText", [] as Object[])
            if (expression.type.genericsTypes.size()!=0) {
                used = true
                println "GenericsRuntime.makeMeGeneric(<MAPA>, ${returned}"
            }
            returned
        }
     */
}
