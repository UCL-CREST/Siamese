    public static Object[] toOneDimension(Object[][] argTwoDimArray) {
        if (argTwoDimArray == null) {
            return null;
        }
        Object[] src;
        int size = 0;
        for (Object[] element : argTwoDimArray) {
            src = element;
            if (src != null) {
                size += src.length;
            }
        }
        Class<?> arrayClass = argTwoDimArray.getClass().getComponentType().getComponentType();
        Object[] results = (Object[]) Array.newInstance(arrayClass, size);
        int nextElement = 0;
        int srcLength;
        for (Object[] element : argTwoDimArray) {
            src = element;
            if (src != null) {
                srcLength = src.length;
                System.arraycopy(src, 0, results, nextElement, srcLength);
                nextElement += srcLength;
            }
        }
        return results;
    }
