    public static <T> T[] insertIntoArray(T[] array, int index, T newValue) {
        @SuppressWarnings("unchecked") T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        if (index > 0) System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = newValue;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }
