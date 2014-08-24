package can.i.has.generics.compilation

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE, ElementType.METHOD])
@GroovyASTTransformationClass(["can.i.has.generics.compilation.UsesGenericsTransform"])
//todo: to disable generics for one method in class with this adnotation, should we use "excludes" adnotation argument or @UsesNoGenerics ?
public @interface UsesGenerics {}