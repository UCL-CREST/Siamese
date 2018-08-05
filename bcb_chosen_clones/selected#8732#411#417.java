    @Override
    public <T> T[] toArray(T[] a) {
        T[] r = a.length >= size ? a : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        if (size > 0) System.arraycopy(elementData, 0, r, 0, size);
        if (r.length > size) r[size] = null;
        return r;
    }
