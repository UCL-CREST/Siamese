    @SuppressWarnings({ "unused", "unchecked" })
    public static <T> T[] insert(T[] array, int position, T value) {
        Assert.notNull(array);
        Assert.inRange(position, 0, array.length);
        T[] result = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        if (position > 0) System.arraycopy(array, 0, result, 0, position);
        if (position < array.length) System.arraycopy(array, position, result, position + 1, array.length - position);
        result[position] = value;
        return result;
    }
