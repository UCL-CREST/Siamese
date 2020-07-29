    public static <T> T[] appendToArrayBegining(T[] array, T element, T... elements) {
        Class<?> componentType = array.getClass().getComponentType();
        Object newArray = Array.newInstance(componentType, array.length + 1 + elements.length);
        Array.set(newArray, 0, element);
        if (elements.length > 0) {
            System.arraycopy(elements, 0, newArray, 1, elements.length);
            System.arraycopy(array, 0, newArray, elements.length, array.length);
        } else {
            System.arraycopy(array, 0, newArray, 1, array.length);
        }
        return (T[]) newArray;
    }
