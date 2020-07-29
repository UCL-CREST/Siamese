    private static Object extendArray(Object a1) {
        int n = Array.getLength(a1);
        Object a2 = Array.newInstance(a1.getClass().getComponentType(), n + ARRAY_SIZE_INCREMENT);
        System.arraycopy(a1, 0, a2, 0, n);
        return a2;
    }
