    public static <T> T[] getExtendedArray(final T[] array, final int index, final T element) {
        @SuppressWarnings("unchecked") final T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1, newArray.length - (index + 1));
        return newArray;
    }
