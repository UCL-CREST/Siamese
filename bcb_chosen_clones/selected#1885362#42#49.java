    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] oldArray, T[] toAppend) {
        Class<?> component = oldArray.getClass().getComponentType();
        T[] array = (T[]) Array.newInstance(component, oldArray.length + toAppend.length);
        System.arraycopy(oldArray, 0, array, 0, oldArray.length);
        System.arraycopy(toAppend, 0, array, oldArray.length, toAppend.length);
        return array;
    }
