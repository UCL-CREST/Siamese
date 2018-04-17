    @SuppressWarnings("unchecked")
    public static <T> T[] remove(T[] array, int position) {
        Assert.notNull(array);
        Assert.inRange(position, 0, array.length - 1);
        T[] result = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), array.length - 1);
        if (position > 0) System.arraycopy(array, 0, result, 0, position);
        if (position < array.length - 1) System.arraycopy(array, position + 1, result, position, array.length - (position + 1));
        return result;
    }
