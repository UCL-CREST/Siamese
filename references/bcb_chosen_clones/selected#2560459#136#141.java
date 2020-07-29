    public static Object[] remove(Object[] source, int begin, int end) {
        Object[] copy = (Object[]) Array.newInstance(source.getClass().getComponentType(), source.length - (end - begin));
        System.arraycopy(source, 0, copy, 0, begin);
        System.arraycopy(source, end + 1, copy, begin, source.length - (end - begin));
        return copy;
    }
