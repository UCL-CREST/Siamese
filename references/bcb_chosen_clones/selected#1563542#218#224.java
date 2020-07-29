    public static <T> T[] addToArray(T[] array1, T[] array2) {
        Class<?> componentType = array1.getClass().getComponentType();
        Object newArray = Array.newInstance(componentType, array1.length + array2.length);
        System.arraycopy(array1, 0, newArray, 0, array1.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return (T[]) newArray;
    }
