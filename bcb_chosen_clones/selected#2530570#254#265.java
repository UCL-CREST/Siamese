    public static Object[] setSize(Object[] array, int newSize) {
        if (array.length == newSize) {
            return array;
        }
        Object[] tmp = (Object[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), newSize);
        if (newSize > array.length) {
            System.arraycopy(array, 0, tmp, 0, array.length);
        } else {
            System.arraycopy(array, 0, tmp, 0, newSize);
        }
        return tmp;
    }
