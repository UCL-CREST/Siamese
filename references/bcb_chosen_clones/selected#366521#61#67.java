    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] array0, T[] array1) {
        T[] array2 = (T[]) Array.newInstance(array0.getClass().getComponentType(), array0.length + array1.length);
        System.arraycopy(array0, 0, array2, 0, array0.length);
        System.arraycopy(array1, 0, array2, array0.length, array1.length);
        return array2;
    }
