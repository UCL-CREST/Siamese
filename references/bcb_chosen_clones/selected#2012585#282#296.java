    @SuppressWarnings("unchecked")
    public static <T> T[] remove(final T[] array, final int from, final int to) {
        assert (to >= from) : to + " - " + from;
        int length = getLength(array);
        if (from < 0 || to >= length) {
            throw new IndexOutOfBoundsException("from: " + from + ", to: " + to + ", Length: " + length);
        }
        int remsize = to - from + 1;
        Object result = Array.newInstance(array.getClass().getComponentType(), length - remsize);
        System.arraycopy(array, 0, result, 0, from);
        if (to < length - 1) {
            System.arraycopy(array, to + 1, result, from, length - to - 1);
        }
        return (T[]) result;
    }
