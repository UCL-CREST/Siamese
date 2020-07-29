    public static Object[] realloc(Object[] data, int newSize) {
        if (newSize == data.length) return data;
        Object[] result = (Object[]) java.lang.reflect.Array.newInstance(data.getClass().getComponentType(), newSize);
        System.arraycopy(data, 0, result, 0, newSize < data.length ? newSize : data.length);
        return result;
    }
