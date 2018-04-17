    @SuppressWarnings("unchecked")
    public static <T> T[] resize(T[] array, int newSize) {
        Class<?> type = array.getClass().getComponentType();
        T[] newArray = (T[]) Array.newInstance(type, newSize);
        int toCopy = Math.min(array.length, newArray.length);
        System.arraycopy(array, 0, newArray, 0, toCopy);
        return newArray;
    }
