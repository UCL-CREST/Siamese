    @SuppressWarnings("unchecked")
    public static <T> T[] add(T[] array, T value) {
        T[] tmp = (T[]) java.lang.reflect.Array.newInstance(((Class<? extends T>) array.getClass()).getComponentType(), array.length + 1);
        System.arraycopy(array, 0, tmp, 0, array.length);
        tmp[array.length] = value;
        return tmp;
    }
