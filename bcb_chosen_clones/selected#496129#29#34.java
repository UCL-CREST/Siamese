    public static Object expand(Object src, int increase, boolean bottom) {
        int size = Array.getLength(src);
        Object dest = Array.newInstance(src.getClass().getComponentType(), size + increase);
        System.arraycopy(src, 0, dest, (bottom) ? 0 : increase, size);
        return dest;
    }
