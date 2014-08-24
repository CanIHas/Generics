package can.i.has.generics.runtime

import can.i.has.generics.model.GenericMethodDescriptor

import groovy.json.JsonSlurper

import can.i.has.generics.model.GenericClassDescriptor

import java.lang.reflect.Method

@Singleton
class GenericParametrizationRepository {
    static JsonSlurper slurper = new JsonSlurper()

    void checkArgumentTypes(Object object, String name, Object[] args){
        def genericsDescriptors = getGenericsDescriptorsForType(object.class)
        def matchingMethods = object.class.methods.findAll {
            it.name == name
        }.findAll {
            matchesArguments(it, args)
        }
        assert matchingMethods.size() == 1
        def method = matchingMethods[0]
        def genericMethodDescriptor = findGenericMethodDescriptor(method, genericsDescriptors)
        if (genericMethodDescriptor)
            checkCallAgainstDescriptor(object, args, genericMethodDescriptor, method.varArgs ? method.parameterTypes.size()-1 : -1)
    }

    List<GenericClassDescriptor> getGenericsDescriptorsForType(Class clazz){
        clazz.interfaces.collect(this.&getGenericsDescriptorsForInterface).flatten()
    }

    List<GenericClassDescriptor> getGenericsDescriptorsForInterface(Class clazz){
        if (!(clazz.interface))
            assert false //todo
        def name = clazz.canonicalName
        def parts = name.split(/[.]/).toList()
        URL url;
        def i = (0..(parts.size()-1)).find { int idx ->
            url = this.class.classLoader.getResource("generics/${parts[0..idx].join(File.separator)}.generics.json")
        }
        if (url==null)
            return []
        List<GenericClassDescriptor> parsed = [ GenericClassDescriptor.load(parts[i], slurper.parseText(url.text)) ]
        if (i<parts.size()-1) {
            def range = i+1..parts.size()-1
            parts[range].each { String part ->
                parsed = [ parsed[0].subclasses[part] ]
            }
        }
        parsed + (clazz.interfaces.collect(this.&getGenericsDescriptorsForInterface))
    }

    private static class HelperException extends RuntimeException {}

    boolean matchesArguments(Method method, Object[] args){
        // method signature should be either the same length as arguments array, or shorter, but only if varArgs==true
        // if varArgs==true, but signature and args array have the same length, varArgs isn't considered to be used
        boolean varArgsUsed = method.varArgs && method.parameterTypes.size() < args.size()
        boolean equalLengths = method.parameterTypes.size() == args.size()
        if (!( varArgsUsed || equalLengths ))
            return false
        try {
            method.parameterTypes.eachWithIndex { Class clazz, int i->
                if (!(clazz.isInstance(args[i])))
                    throw new HelperException()
            }
            if (varArgsUsed)
                (method.parameterTypes.size()..args.size()).each { int i ->
                    if (!(method.parameterTypes.last().isInstance(args[i])))
                        throw new HelperException()
                }
        } catch (HelperException error) {return false}
        true
    }

    /**
     *
     * @param object
     * @param args
     * @param descriptor result of findGenericMethodDescriptor(...)
     * @param varargsIdx
     */
    void checkCallAgainstDescriptor(Object object, Object[] args, Map descriptor, int varargsIdx){

        //genericTypes is statically added to Object with metaclass in static initialization block of GenericsRuntime
//        if descriptor.methods.find{ it. }

        //todo; code running now was compiled with different interface definition that compiled for this descriptor
        assert descriptor.params as Set == object.genericTypes.keySet()
        def m = descriptor.method as GenericMethodDescriptor
        m.actualSignature.eachWithIndex { String className, int i ->
            def clazz = descriptor.params.contains(className) ? object.genericTypes[className] : Class.forName(className)
            assert clazz.isInstance(args[i]) //todo
        }
        if (varargsIdx>=0) {
            assert m.actualSignature.size()<args.size()
            assert varargsIdx<args.size()
            def lastClazz = descriptor.params.contains(className) ?
                object.genericTypes[className] :
                Class.forName(m.actualSignature.last())
            args[varargsIdx..(args.size()-1)].each { Object arg ->
                assert lastClazz.isInstace(arg) //todo
            }
        }
    }


    Map findGenericMethodDescriptor(Method method, List<GenericClassDescriptor> descriptors){
//        def methodsWithThatName = descriptors.collect {it.methods.}.flatten().findAll
//                { methodDescriptor -> methodDescriptor.name==method.name }


        def matchedMethods = descriptors.collect { GenericClassDescriptor c ->
            c.methods.findAll { GenericMethodDescriptor m ->
                m.name==method.name
            }.findAll { GenericMethodDescriptor m ->
                method.parameterTypes.collect {
                    it.canonicalName
                } == m.actualSignature.collect {
                    c.parameters.contains(it) ? Object.canonicalName : it
                }
            }.collect { GenericMethodDescriptor m ->
                [
                    method: m,
                    params: c.parameters
                ]
            }
        }.flatten()
        assert matchedMethods.size() < 2
        return matchedMethods ? matchedMethods[0] : null
    }

    void checkReturnType(Object object, String name, Object[] args, Object result){

    }
}
