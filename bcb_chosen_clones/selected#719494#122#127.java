    @SuppressWarnings("unchecked")
    public static final <T> T[] subArray(final T[] array, final int offset, final int length) {
        final T[] newArray;
        System.arraycopy(array, offset, newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), length), 0, length);
        return newArray;
    }
