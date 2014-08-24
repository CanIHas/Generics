package can.i.has.has.generics.compilation


class UsesGenericsTransformTest extends GroovyTestCase {

    void testConstructorExpressionOnMethod(){
        def genericObject = new GroovyShell(ClassLoader.systemClassLoader).parse('''import can.i.has.generics.compilation.UsesGenerics

class Tested {
    @UsesGenerics
    List<String> foo() {
        return new ArrayList<String>();
    }
}
new Tested()
''').foo()
        assert genericObject.genericTypes == [E: Object]
    }
}
