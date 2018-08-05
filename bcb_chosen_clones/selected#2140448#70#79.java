    public static Object setLength(Object array, int newLength) {
        if (array == null) {
            return null;
        }
        int oldLength = Array.getLength(array);
        if (newLength == oldLength) return array;
        Object t = Array.newInstance(array.getClass().getComponentType(), newLength);
        System.arraycopy(array, 0, t, 0, oldLength < newLength ? oldLength : newLength);
        return t;
    }
