package can.i.has.generics.model

import groovy.transform.Canonical

@Canonical
class GenericMethodDescriptor {
    String name
    List<String> actualSignature
//    Map<String, List<Integer>> genericIdxs
//    boolean varargs // we need to move varargs check to runtime, since there is no info on that in AST

    Map getJson(){
        [
            name: name,
            args: actualSignature,
//            genericIdxs: genericIdxs
        ]
    }

    static GenericMethodDescriptor load(Map json){
        new GenericMethodDescriptor(json.name, json.args)
    }
}
