    @SuppressWarnings("unchecked")
    public static <T> T[] addToArray(T[] array, T el) {
        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = el;
        return newArray;
    }
