    public static Object[] append(Object[] source, Object obj) {
        Object[] copy = (Object[]) Array.newInstance(source.getClass().getComponentType(), source.length + 1);
        System.arraycopy(source, 0, copy, 0, source.length);
        copy[source.length] = obj;
        return copy;
    }
