    public static Object resizeArray(Object source, int newLength) {
        Class compType = source.getClass().getComponentType();
        int sourceLength = Array.getLength(source);
        Object target = Array.newInstance(compType, newLength);
        if (newLength < sourceLength) {
            sourceLength = newLength;
        }
        System.arraycopy(source, 0, target, 0, sourceLength);
        return target;
    }
