    public static Object[] insert(Object[] source, int index, Object obj) {
        Object[] copy = (Object[]) Array.newInstance(source.getClass().getComponentType(), source.length + 1);
        System.arraycopy(source, 0, copy, 0, index);
        System.arraycopy(source, index, copy, index + 1, source.length - index);
        copy[index] = obj;
        return copy;
    }
