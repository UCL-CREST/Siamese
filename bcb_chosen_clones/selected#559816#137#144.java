    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size());
        E[] elementData = (E[]) toArray();
        System.arraycopy(elementData, 0, a, 0, size());
        if (a.length > size()) a[size()] = null;
        return a;
    }
