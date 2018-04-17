    public static <T> T[] ensureCapacity(T[] array, int newCapacity) {
        if (array.length >= newCapacity) {
            return array;
        }
        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), newCapacity);
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }
