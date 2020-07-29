    public static Object[] concatenated(Object[] xs, Object[] ys) {
        Object[] xsys = (Object[]) java.lang.reflect.Array.newInstance(xs.getClass().getComponentType(), xs.length + ys.length);
        System.arraycopy(xs, 0, xsys, 0, xs.length);
        System.arraycopy(ys, 0, xsys, xs.length, ys.length);
        return xsys;
    }
