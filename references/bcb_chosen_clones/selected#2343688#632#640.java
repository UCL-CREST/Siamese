    public static Object resizeArray(Object source, int newsize) {
        Object newarray = Array.newInstance(source.getClass().getComponentType(), newsize);
        int oldsize = Array.getLength(source);
        if (oldsize < newsize) {
            newsize = oldsize;
        }
        System.arraycopy(source, 0, newarray, 0, newsize);
        return newarray;
    }
