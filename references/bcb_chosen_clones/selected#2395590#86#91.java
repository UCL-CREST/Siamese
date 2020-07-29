    static final Object[] resizeArray(Object[] src, int new_size) {
        Class compClass = src.getClass().getComponentType();
        Object tmp[] = (Object[]) Array.newInstance(compClass, new_size);
        System.arraycopy(src, 0, tmp, 0, (src.length < new_size ? src.length : new_size));
        return tmp;
    }
