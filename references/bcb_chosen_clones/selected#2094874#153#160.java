    @SuppressWarnings("unchecked")
    public static <T> T[] appendArray(T[] array, T value) {
        Class<?> type = array.getClass().getComponentType();
        T[] newArray = (T[]) Array.newInstance(type, array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[newArray.length - 1] = value;
        return newArray;
    }
