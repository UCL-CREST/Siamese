    @SuppressWarnings("unchecked")
    public static final <E> E[] resized(E[] array, int length) {
        E[] result = array;
        if (array.length != length) {
            result = (E[]) Array.newInstance(array.getClass().getComponentType(), length);
            int minLength = Math.min(array.length, length);
            System.arraycopy(array, 0, result, 0, minLength);
        }
        return result;
    }
