    public static Object resizeArrayIfDifferent(Object source, int newsize) {
        int oldsize = Array.getLength(source);
        if (oldsize == newsize) {
            return source;
        }
        Object newarray = Array.newInstance(source.getClass().getComponentType(), newsize);
        if (oldsize < newsize) {
            newsize = oldsize;
        }
        System.arraycopy(source, 0, newarray, 0, newsize);
        return newarray;
    }
