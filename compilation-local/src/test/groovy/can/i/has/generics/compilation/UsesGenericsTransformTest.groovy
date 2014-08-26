package can.i.has.generics.compilation


class UsesGenericsTransformTest extends GroovyTestCase {

//    void testConstructorExpressionOnMethod(){
//        def genericObject = new GroovyShell(this.class.classLoader).parse('''package can.i.has.generics.compilation
////import can.i.has.generics.compilation.UsesGenerics
//
//class Tested {
//    @UsesGenerics
//    List<String> foo() {
//        return new ArrayList<String>();
//    }
//}
//new Tested()
//''').run().foo()
//        assert genericObject.genericTypes == [E: Object]
//    }

    void testCastExpressionOnMethod(){
        def genericObject = new GroovyShell(this.class.classLoader).parse('''package can.i.has.generics.compilation
//import can.i.has.generics.compilation.UsesGenerics

class Tested {
    @UsesGenerics
    List<String> foo() {
        return [] as ArrayList<String>;
    }
}
new Tested()
''').run().foo()
        assert genericObject.genericTypes == [E: String]
    }
}
