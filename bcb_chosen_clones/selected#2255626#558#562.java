    private Object reallocArray(Object src, int newSize) {
        Object dest = java.lang.reflect.Array.newInstance(src.getClass().getComponentType(), newSize);
        System.arraycopy(src, 0, dest, 0, java.lang.reflect.Array.getLength(src));
        return dest;
    }
