    public static <T> T[] remove(T[] array, int index) {
        Arrays.validateIndex(index);
        if (array == null) {
            return null;
        }
        if (index >= array.length) {
            return array;
        }
        @SuppressWarnings("unchecked") final T[] result = Arrays.createArray((Class<? extends T>) array.getClass().getComponentType(), array.length - 1);
        System.arraycopy(array, 0, result, 0, index);
        System.arraycopy(array, index + 1, result, index, array.length - index - 1);
        return result;
    }
