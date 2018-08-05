    public static final Object addElementToArray(Object array, Object item) {
        Class fc = array.getClass().getComponentType();
        int n = Array.getLength(array);
        Object newArray = Array.newInstance(fc, n + 1);
        if (n > 0) System.arraycopy(array, 0, newArray, 0, n);
        Array.set(newArray, n, item);
        return newArray;
    }
