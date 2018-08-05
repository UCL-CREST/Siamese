    public <T> T[] toArray(T[] a) {
        Object[] elementData = array();
        if (a.length < elementData.length) a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), elementData.length);
        System.arraycopy(elementData, 0, a, 0, elementData.length);
        if (a.length > elementData.length) a[elementData.length] = null;
        return a;
    }
