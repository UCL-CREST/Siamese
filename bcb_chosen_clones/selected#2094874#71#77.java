    @SuppressWarnings("unchecked")
    public static <T> T[] copy(T[] array) {
        Class<?> type = array.getClass().getComponentType();
        T[] newArray = (T[]) Array.newInstance(type, array.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }
