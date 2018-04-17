    public static Object[] removeFromArray(Object[] array, int i) {
        if (i < 0 || i >= array.length) {
            throw new IndexOutOfBoundsException();
        }
        Object[] newArray = (Object[]) Array.newInstance(array.getClass().getComponentType(), array.length - 1);
        System.arraycopy(array, 0, newArray, 0, i);
        System.arraycopy(array, i + 1, newArray, i, array.length - i - 1);
        return newArray;
    }
