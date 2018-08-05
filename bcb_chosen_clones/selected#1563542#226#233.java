    public static <T> T[] appendToArrayEnd(T[] array, T element, T... elements) {
        Class<?> componentType = array.getClass().getComponentType();
        Object newArray = Array.newInstance(componentType, array.length + 1 + elements.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        Array.set(newArray, array.length, element);
        System.arraycopy(elements, 0, newArray, array.length + 1, elements.length);
        return (T[]) newArray;
    }
