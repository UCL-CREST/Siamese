    public static Object copyArray(Object array) {
        int len = Array.getLength(array);
        Object copy = Array.newInstance(array.getClass().getComponentType(), len);
        System.arraycopy(array, 0, copy, 0, len);
        return copy;
    }
