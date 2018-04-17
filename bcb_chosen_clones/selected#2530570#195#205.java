    public static Object[] concat(Object[] array1, Object[] array2) {
        if (array1 == null) {
            return array2;
        } else if (array2 == null) {
            return array1;
        }
        Object[] tmp = (Object[]) java.lang.reflect.Array.newInstance(array1.getClass().getComponentType(), array1.length + array2.length);
        System.arraycopy(array1, 0, tmp, 0, array1.length);
        System.arraycopy(array2, 0, tmp, array1.length, array2.length);
        return tmp;
    }
