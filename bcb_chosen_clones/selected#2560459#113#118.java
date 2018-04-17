    public static Object[] remove(Object[] source, int index) {
        Object[] copy = (Object[]) Array.newInstance(source.getClass().getComponentType(), source.length - 1);
        System.arraycopy(source, 0, copy, 0, index);
        System.arraycopy(source, index + 1, copy, index, source.length - index - 1);
        return copy;
    }
