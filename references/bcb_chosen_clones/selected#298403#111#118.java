    @SuppressWarnings("unchecked")
    private static <T> T[] appendArrays(T[] array1, T[] array2) {
        Class<?> array1Class = array1.getClass();
        T[] newArray = (T[]) Array.newInstance(array1Class.getComponentType(), array1.length + array2.length);
        System.arraycopy(array1, 0, newArray, 0, array1.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }
