    @SuppressWarnings("unchecked")
    public static final <E> E[] subArray(E[] array, int start, int end) {
        E[] result = (E[]) Array.newInstance(array.getClass().getComponentType(), end - start);
        System.arraycopy(array, start, result, 0, end - start);
        return result;
    }
