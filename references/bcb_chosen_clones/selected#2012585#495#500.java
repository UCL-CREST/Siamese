    public static Object resize(final Object[] ary, final int length) {
        final Object newary = Array.newInstance(ary.getClass().getComponentType(), length);
        final int copysize = length > ary.length ? length : ary.length;
        System.arraycopy(ary, 0, newary, 0, copysize);
        return newary;
    }
