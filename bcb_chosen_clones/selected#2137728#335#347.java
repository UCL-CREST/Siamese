    @SuppressWarnings({ "unchecked", "hiding" })
    public <E> E[] toArray(E[] a) {
        E[] array = (E[]) toArray();
        int size = array.length;
        if (a.length < size()) {
            a = (E[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(array, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
