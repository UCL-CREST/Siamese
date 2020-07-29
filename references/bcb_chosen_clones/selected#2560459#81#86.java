    public static Object[] concat(Object[] source, Object[] objs) {
        Object[] copy = (Object[]) Array.newInstance(source.getClass().getComponentType(), source.length + objs.length);
        System.arraycopy(source, 0, copy, 0, source.length);
        System.arraycopy(objs, 0, copy, source.length, objs.length);
        return copy;
    }
