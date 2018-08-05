    private static <T> T[] cloneSubarray(T[] a, int from, int to) {
        int n = to - from;
        T[] result = (T[]) Array.newInstance(a.getClass().getComponentType(), n);
        System.arraycopy(a, from, result, 0, n);
        return result;
    }
