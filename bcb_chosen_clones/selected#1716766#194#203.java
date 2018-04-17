    @SuppressWarnings("unchecked")
    public <S> S[] toArray(S[] a) {
        a = (S[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size());
        Object[] elementData = toArray();
        System.arraycopy(elementData, 0, a, 0, size());
        if (a.length > size()) {
            a[size()] = null;
        }
        return a;
    }
