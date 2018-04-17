    @SuppressWarnings("unchecked")
    private <T> T[] newArray(T[] array) {
        int size = size();
        if (size > array.length) {
            Class<?> clazz = array.getClass().getComponentType();
            array = (T[]) Array.newInstance(clazz, size);
        }
        if (front < rear) {
            System.arraycopy(elements, front, array, 0, size);
        } else if (size != 0) {
            int length = elements.length;
            System.arraycopy(elements, front, array, 0, length - front);
            System.arraycopy(elements, 0, array, length - front, rear);
        }
        if (size < array.length) {
            array[size] = null;
        }
        return array;
    }
