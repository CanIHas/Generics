package pkg


interface ExampleInterface<T> {
    T add(T other)
    void doMe(int x, T t)
    static interface SubExampleInterface<E> {
        void nuffin(t, E e)
        interface SubSubExampleInterface<F> {
            F dupa (e, t)
        }
    }
    interface NonGeneric {}
}
