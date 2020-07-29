    public static Object[] convert(Object[] from, Object[] to) {
        if (to.length < from.length) {
            to = (Object[]) Array.newInstance(to.getClass().getComponentType(), from.length);
        }
        System.arraycopy(from, 0, to, 0, from.length);
        if (to.length > from.length) {
            to[from.length] = null;
        }
        return to;
    }
