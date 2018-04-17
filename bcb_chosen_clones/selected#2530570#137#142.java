    public static Object[] add(Object[] array, Object value) {
        Object[] tmp = (Object[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, tmp, 0, array.length);
        tmp[array.length] = value;
        return tmp;
    }
