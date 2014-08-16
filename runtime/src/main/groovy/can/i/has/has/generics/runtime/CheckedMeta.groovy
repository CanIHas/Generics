package can.i.has.has.generics.runtime

class CheckedMeta extends ProxyMetaClass{

    static ProxyMetaClass getInstance(Class theClass) {
        MetaClassRegistry metaRegistry = GroovySystem.getMetaClassRegistry();
        MetaClass meta = metaRegistry.getMetaClass(theClass);
        return new CheckedMeta(metaRegistry, theClass, meta);
    }

    /**
     * @param adaptee the MetaClass to decorate with interceptability
     */ CheckedMeta(MetaClassRegistry registry, Class theClass, MetaClass adaptee) {
        super(registry, theClass, adaptee)
        this.interceptor = new Interceptor() {
            @Override
            Object beforeInvoke(Object object, String methodName, Object[] arguments) {
                GenericParametrizationRepository.instance.checkArgumentTypes(object, methodName, arguments)
            }

            @Override
            Object afterInvoke(Object object, String methodName, Object[] arguments, Object result) {
                GenericParametrizationRepository.instance.checkReturnType(object, methodName, arguments, result)
                result
            }

            @Override
            boolean doInvoke() {
                true
            }
        }
    }


}
