package can.i.has.has.generics.runtime

class GenericsRuntime {
    static {
        Object.metaClass.genericTypes = [:]
    }

    static <T> T makeMeGeneric(Map<String, Class> parameters, T t){
        t.metaClass = CheckedMeta.getInstance(t.class)
        t.genericTypes = parameters
        t
    }
}
