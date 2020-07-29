    public static Object resizeArray(final Object oldArray, final int minimumSize) {
        final Class<?> cls = oldArray.getClass();
        if (!cls.isArray()) {
            return null;
        }
        final int oldLength = Array.getLength(oldArray);
        int newLength = oldLength + (oldLength / 2);
        if (newLength < minimumSize) {
            newLength = minimumSize;
        }
        final Class<?> componentType = oldArray.getClass().getComponentType();
        final Object newArray = Array.newInstance(componentType, newLength);
        System.arraycopy(oldArray, 0, newArray, 0, oldLength);
        return newArray;
    }
