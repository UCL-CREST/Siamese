    public static Object resizeArray(Object old, int newLength) {
        int oldLength = Array.getLength(old);
        Class elementType = old.getClass().getComponentType();
        Object newArray = Array.newInstance(elementType, newLength);
        int upto = (oldLength < newLength) ? oldLength : newLength;
        System.arraycopy(old, 0, newArray, 0, upto);
        return newArray;
    }
