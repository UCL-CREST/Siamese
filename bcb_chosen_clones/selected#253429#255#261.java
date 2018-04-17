    public static <T> T[] catenate(T[] A, T[] B) {
        Class<T> eltClass = (Class<T>) A.getClass().getComponentType();
        T[] C = (T[]) Array.newInstance(eltClass, A.length + B.length);
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);
        return C;
    }
