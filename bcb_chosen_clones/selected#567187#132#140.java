    public static <T> T[] clone(T[] array) {
        if (array == null) return null;
        if (array.length == 0) {
            return (T[]) Array.newInstance(array.getClass().getComponentType(), 0);
        }
        T[] tmp = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), array.length);
        System.arraycopy(array, 0, tmp, 0, tmp.length);
        return tmp;
    }
