    public static final Object concat(Object ary, Object ary1) {
        int len = Array.getLength(ary) + Array.getLength(ary1);
        if (!ary.getClass().getComponentType().isAssignableFrom(ary1.getClass().getComponentType())) throw new IllegalArgumentException("These concated array component types are not compatible.");
        Object dst = Array.newInstance(ary.getClass().getComponentType(), len);
        System.arraycopy(ary, 0, dst, 0, Array.getLength(ary));
        System.arraycopy(ary1, 0, dst, Array.getLength(ary), Array.getLength(ary1));
        return dst;
    }
