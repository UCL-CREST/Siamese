    public Object[] toArray(Object[] a) {
        int size = size();
        if (a.length < size) {
            a = (Object[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(toArray(), 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
