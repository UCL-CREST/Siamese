    public static final Object resize(Object ary, int size) {
        final int oldsz = Array.getLength(ary);
        if (oldsz == size) return ary;
        final Object dst = Array.newInstance(ary.getClass().getComponentType(), size);
        System.arraycopy(ary, 0, dst, 0, oldsz > size ? size : oldsz);
        return dst;
    }
