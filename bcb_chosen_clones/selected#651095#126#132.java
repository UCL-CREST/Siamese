    @SuppressWarnings("unchecked")
    public static <T> T[] prependToArray(T[] array, T el) {
        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, newArray, 1, array.length);
        newArray[0] = el;
        return newArray;
    }
