package can.i.has.generics.runtime

@Singleton
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
