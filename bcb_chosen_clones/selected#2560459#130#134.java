    public static Object[] setLength(Object[] source, int length) {
        Object[] copy = (Object[]) Array.newInstance(source.getClass().getComponentType(), length);
        System.arraycopy(source, 0, copy, 0, Math.min(source.length, length));
        return copy;
    }
