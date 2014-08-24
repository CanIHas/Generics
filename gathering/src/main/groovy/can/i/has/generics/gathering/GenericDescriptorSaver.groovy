package can.i.has.has.generics.gathering

import can.i.has.has.generics.model.GenericClassDescriptor

import groovy.json.JsonBuilder


class GenericDescriptorSaver {
    Map batch
    File rootDir

    GenericDescriptorSaver(){
        batch = [:]
        Properties p = new Properties()
        def stream = this.class.classLoader.getResourceAsStream("generics/config.properties")
        if (stream)
            p.load(stream)
        rootDir = new File(
            p.getProperty("generics.destination",
                System.properties.getProperty("generics.destination",
                    "./genericsDestination"
                )
            )
        )
        rootDir.mkdirs()
    }

    static Map buildRecursiveMap(){
        [:].withDefault { k -> buildRecursiveMap() }
    }

    void addClass(GenericClassDescriptor clazz){
        def parts = clazz.qualifiedName.split("[.]").toList()
        def pointer = batch
        parts.eachWithIndex { String entry, int i ->
            if (i<parts.size()-1)
                pointer = pointer."$entry"
            else
                pointer."$entry" = clazz
        }
    }

    GenericDescriptorSaver(File rootDir){
        this.rootDir=rootDir
    }

    void openBatch(){
        batch = buildRecursiveMap()
    }

    void closeBatch(){
        walk(batch) { GenericClassDescriptor clazz ->
            def target = targetFileForClass(clazz.qualifiedName)
            target.parentFile.mkdirs()
            target.text = new JsonBuilder(clazz.json).toPrettyString()
        }
    }

    void walk(Map m, Closure c){
        m.values().each {
            if (it instanceof Map)
                walk(it, c)
            else
                c(it)
        }
    }

    File targetFileForClass(String clazz){
        new File(rootDir, "${clazz.replaceAll("[.]", File.separator)}.generics.json")
    }
}
