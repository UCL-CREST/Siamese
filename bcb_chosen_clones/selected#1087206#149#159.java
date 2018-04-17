    public Object[] toArray(Object a[]) {
        Object[] array = a;
        if (array.length < items.length) {
            array = (Object[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), items.length);
        }
        System.arraycopy(items, 0, array, 0, items.length);
        if (array.length > items.length) {
            array[items.length] = null;
        }
        return array;
    }
