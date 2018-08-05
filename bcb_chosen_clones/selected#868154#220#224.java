    public static <T> T[] arrayCopy(T[] array) {
        T[] copy = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);
        System.arraycopy(array, 0, copy, 0, array.length);
        return copy;
    }
