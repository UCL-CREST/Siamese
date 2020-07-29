    public static Object[] section(Object[] xs, int start, int limit) {
        Object[] xs_ = (Object[]) java.lang.reflect.Array.newInstance(xs.getClass().getComponentType(), limit - start);
        System.arraycopy(xs, start, xs_, 0, xs_.length);
        return xs_;
    }
