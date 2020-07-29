    @SuppressWarnings("unchecked")
    public <U extends Object> U[] toArray(U[] a) {
        if ((a != null) && (a.length >= size)) {
            System.arraycopy(data, pos, a, 0, size);
            if (a.length > size) a[size] = null;
            return a;
        }
        U[] b = (U[]) Array.newInstance(a.getClass().getComponentType(), size);
        System.arraycopy(data, pos, b, 0, size);
        return b;
    }
