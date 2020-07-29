    @SuppressWarnings("unchecked")
    public static final <T> T[] add(final T[] a1, final T... a2) {
        if (a1 == null) return a2 == null ? null : a2.clone();
        if (a2 == null) return a1.clone();
        final T[] a = (T[]) Array.newInstance(a1.getClass().getComponentType(), a1.length + a2.length);
        System.arraycopy(a1, 0, a, 0, a1.length);
        System.arraycopy(a2, 0, a, a1.length, a2.length);
        return a;
    }
