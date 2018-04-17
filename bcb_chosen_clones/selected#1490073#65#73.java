    public static Object setLength(Object array, int newLength) {
        if (array == null) {
            return null;
        }
        Object t = Array.newInstance(array.getClass().getComponentType(), newLength);
        int oldLength = Array.getLength(array);
        System.arraycopy(array, 0, t, 0, oldLength < newLength ? oldLength : newLength);
        return t;
    }
