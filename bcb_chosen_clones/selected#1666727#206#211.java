    public Object[] toArray(Object[] a) {
        if (a.length < size) a = (Object[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }
