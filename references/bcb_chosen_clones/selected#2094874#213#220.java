    @SuppressWarnings("unchecked")
    public static <T> T[] prependArray(T[] array, T value) {
        Class<?> type = array.getClass().getComponentType();
        T[] newArray = (T[]) Array.newInstance(type, array.length + 1);
        System.arraycopy(array, 0, newArray, 1, array.length);
        newArray[0] = value;
        return newArray;
    }
