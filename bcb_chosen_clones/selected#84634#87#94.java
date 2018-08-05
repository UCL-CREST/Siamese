    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] xs, T x) {
        int n = xs.length;
        T[] t = (T[]) Array.newInstance(xs.getClass().getComponentType(), n + 1);
        System.arraycopy(xs, 0, t, 0, n);
        t[n] = x;
        return t;
    }
