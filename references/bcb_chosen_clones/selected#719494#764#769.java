    @SuppressWarnings("unchecked")
    public static final <T> T[] copy(final T[] array) {
        final T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }
