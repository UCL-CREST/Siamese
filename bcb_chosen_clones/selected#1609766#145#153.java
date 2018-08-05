    private static Object resizeArray(Object array, int length) {
        if (Array.getLength(array) < length) {
            Object newArray = Array.newInstance(array.getClass().getComponentType(), length);
            System.arraycopy(array, 0, newArray, 0, Array.getLength(array));
            return newArray;
        } else {
            return array;
        }
    }
