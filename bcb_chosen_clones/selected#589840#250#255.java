    public static final Object clone(Object ary) {
        final int size = Array.getLength(ary);
        final Object dst = Array.newInstance(ary.getClass().getComponentType(), size);
        System.arraycopy(ary, 0, dst, 0, size);
        return dst;
    }
