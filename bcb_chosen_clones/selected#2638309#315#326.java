    @SuppressWarnings("unchecked")
    private <T> T[] newArray(T[] array) {
        if (size > array.length) {
            Class<?> clazz = array.getClass().getComponentType();
            array = (T[]) Array.newInstance(clazz, size);
        }
        System.arraycopy(elements, 0, array, 0, size);
        if (size < array.length) {
            array[size] = null;
        }
        return array;
    }
