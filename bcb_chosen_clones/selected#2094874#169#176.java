    @SuppressWarnings("unchecked")
    public static <T> T[] appendArrays(T[] array1, T[] array2) {
        Class<?> type = array1.getClass().getComponentType();
        T[] newArray = (T[]) Array.newInstance(type, array1.length + array2.length);
        System.arraycopy(array1, 0, newArray, 0, array1.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }
