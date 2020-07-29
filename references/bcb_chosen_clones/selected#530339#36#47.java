    public static Object resizeArray(Object oldArray, int newSize) {
        if (newSize < 0) {
            return oldArray;
        }
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        if (oldSize == newSize) return oldArray;
        Class<?> elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0) System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        return newArray;
    }
