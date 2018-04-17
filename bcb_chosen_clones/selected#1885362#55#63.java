    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] oldArray, T[] toAppend1, T toAppend2) {
        Class<?> component = oldArray.getClass().getComponentType();
        T[] array = (T[]) Array.newInstance(component, oldArray.length + toAppend1.length + 1);
        System.arraycopy(oldArray, 0, array, 0, oldArray.length);
        System.arraycopy(toAppend1, 0, array, oldArray.length, toAppend1.length);
        array[array.length - 1] = toAppend2;
        return array;
    }
