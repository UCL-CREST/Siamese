    public static Object[] duplicate(Object[] source) {
        Object[] copy = (Object[]) Array.newInstance(source.getClass().getComponentType(), source.length);
        System.arraycopy(source, 0, copy, 0, source.length);
        return copy;
    }
