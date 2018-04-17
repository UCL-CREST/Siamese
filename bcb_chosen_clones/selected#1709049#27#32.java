    public static final Object[] changeSize(Object[] o, int newSize) {
        if (o.length == newSize) return o;
        Object[] temp = (Object[]) java.lang.reflect.Array.newInstance(o.getClass().getComponentType(), newSize);
        if (o.length < newSize) System.arraycopy(o, 0, temp, 0, o.length); else System.arraycopy(o, 0, temp, 0, temp.length);
        return temp;
    }
