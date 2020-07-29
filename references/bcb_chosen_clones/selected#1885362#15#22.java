    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] oldArray, T toAppend) {
        Class<?> component = oldArray.getClass().getComponentType();
        T[] array = (T[]) Array.newInstance(component, oldArray.length + 1);
        System.arraycopy(oldArray, 0, array, 0, oldArray.length);
        array[oldArray.length] = toAppend;
        return array;
    }
