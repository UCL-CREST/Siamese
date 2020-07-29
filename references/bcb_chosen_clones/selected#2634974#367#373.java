    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        System.arraycopy(array, 0, a, 0, size);
        if (a.length > size) Arrays.fill(a, size, a.length, null);
        return (a);
    }
