    public static Object[] append(Object[] v, Object elem) {
        Object[] ret = (Object[]) Array.newInstance(v.getClass().getComponentType(), v.length + 1);
        System.arraycopy(v, 0, ret, 0, v.length);
        ret[v.length] = elem;
        return ret;
    }
