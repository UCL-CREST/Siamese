    @SuppressWarnings("unchecked")
    public static <T> T[] merge(T[] src1, T[] src2) {
        final T[] aCombined = (T[]) java.lang.reflect.Array.newInstance(src1.getClass().getComponentType(), src1.length + src2.length);
        System.arraycopy(src1, 0, aCombined, 0, src1.length);
        System.arraycopy(src2, 0, aCombined, src1.length, src2.length);
        return aCombined;
    }
