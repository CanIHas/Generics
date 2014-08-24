package can.i.has.generics.compilation.examples

import can.i.has.generics.compilation.UsesGenerics

@UsesGenerics
class Example1 {
    void foo(String a, int b, List<String> gen){
        def x = new HashMap<Integer, String>()
        [1, 2, 3] as Set<Integer>
    }
}
