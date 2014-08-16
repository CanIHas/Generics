package can.i.has.has.generics.model

import groovy.transform.Canonical

@Canonical
class GenericClassDescriptor{
    String name
    String qualifiedName
    List<GenericMethodDescriptor> methods = []
    List<String> parameters = []
    Map subclasses = [:]

    def propertyMissing(name){
        subclasses[name]
    }

    def propertyMissing(name, val){
        subclasses[name] = val
    }

    Map getJson(){
        def jsonifiedSubclasses = [:]
        subclasses.each {k, v -> jsonifiedSubclasses[k] = v.json}
        [
            params: parameters,
            methods: methods.collect {it.json}
        ] + ( subclasses.isEmpty() ? [:] : [subclasses : jsonifiedSubclasses] )
    }

    static GenericClassDescriptor load(String qualifiedName, Map json) {
        def subs = [:]
        (json.subclasses?:[:]).each { k, v -> subs[k] = load(v) }
        return new GenericClassDescriptor(
            qualifiedName.split("[.]").toList().last(),
            qualifiedName,
            (json.methods?:[]).collect { GenericMethodDescriptor.load(it) },
            json.params?:[],
            subs
        )
    }
}
