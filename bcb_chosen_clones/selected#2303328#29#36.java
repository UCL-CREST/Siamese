    public static Object[] merge(Object[] data1, Object[] data2) {
        if (data2.length == 0) return data1;
        if (data1.length == 0) return data2;
        Object[] result = (Object[]) java.lang.reflect.Array.newInstance(data1.getClass().getComponentType(), data1.length + data2.length);
        System.arraycopy(data1, 0, result, 0, data1.length);
        System.arraycopy(data2, 0, result, data1.length, data2.length);
        return result;
    }
