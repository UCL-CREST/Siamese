    public static Object expand(Object obj, int i, boolean flag) {
        int j = Array.getLength(obj);
        Object obj1 = Array.newInstance(obj.getClass().getComponentType(), j + i);
        System.arraycopy(obj, 0, obj1, flag ? 0 : i, j);
        return obj1;
    }
