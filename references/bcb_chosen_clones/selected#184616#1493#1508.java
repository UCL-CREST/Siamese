    public static synchronized <T> T[] concat(T[] a, T[] b) {
        assertNotNull(a, "Cannot concatenate a null array reference");
        assertNotNull(b, "Cannot concatenate a null array reference");
        final int alen = a.length;
        final int blen = b.length;
        if (alen == 0) {
            return b;
        }
        if (blen == 0) {
            return a;
        }
        final T[] result = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), alen + blen);
        System.arraycopy(a, 0, result, 0, alen);
        System.arraycopy(b, 0, result, alen, blen);
        return result;
    }
