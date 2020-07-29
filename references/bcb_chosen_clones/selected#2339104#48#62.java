    public static <T> T[] removeFromArray(T[] array, T member) {
        int i;
        for (i = 0; i < array.length; i++) {
            if (array[i] == member) {
                break;
            }
        }
        if (i < array.length) {
            T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length - 1);
            System.arraycopy(array, 0, newArray, 0, i);
            System.arraycopy(array, i + 1, newArray, i, array.length - i - 1);
            return newArray;
        }
        return array;
    }
