    public static final Object removeElementFromArray(Object array, int index) {
        Class fc = array.getClass().getComponentType();
        int n = Array.getLength(array);
        Object newArray = Array.newInstance(fc, n - 1);
        if (index > 0) System.arraycopy(array, 0, newArray, 0, index);
        if (index < (n - 1)) System.arraycopy(array, index + 1, newArray, index, n - index - 1);
        return newArray;
    }
