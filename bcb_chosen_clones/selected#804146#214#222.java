    public Object[] toArray(Object a[]) {
        if (a.length < size()) a = (Object[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size());
        if (inverse) {
            throw new OutOfMemoryError("cannot allocate infinite array");
        }
        System.arraycopy(elements, 0, a, 0, elements.length);
        if (a.length > elements.length) a[elements.length] = null;
        return a;
    }
