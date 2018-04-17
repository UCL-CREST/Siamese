    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] array, T element) {
        T[] array2 = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, array2, 0, array.length);
        array2[array.length] = element;
        return array2;
    }
