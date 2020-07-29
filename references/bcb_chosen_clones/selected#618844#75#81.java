    public static <T> T[] getSubsection(T[] array, int startInclusive, int endExclusive) {
        int length = endExclusive - startInclusive;
        length = length > array.length ? array.length : length;
        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), length);
        System.arraycopy(array, startInclusive, newArray, 0, length);
        return newArray;
    }
