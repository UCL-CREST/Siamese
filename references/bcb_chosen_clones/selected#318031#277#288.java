    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) throws ArrayStoreException, NullPointerException {
        if (a.length < size) {
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
