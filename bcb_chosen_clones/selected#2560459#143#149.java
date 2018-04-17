    public static Object[] replace(Object[] source, int begin, int end, Object obj) {
        Object[] copy = (Object[]) Array.newInstance(source.getClass().getComponentType(), source.length - (end - begin) + 1);
        System.arraycopy(source, 0, copy, 0, begin);
        System.arraycopy(source, end + 1, copy, begin + 1, source.length - (end - begin));
        copy[begin] = obj;
        return copy;
    }
