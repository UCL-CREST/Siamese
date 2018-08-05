    public static Object[] subArray(Object[] objectArray, int startIndex, int length) {
        NullArgumentException.check(objectArray);
        if ((startIndex == 0) && (length == objectArray.length)) {
            return objectArray;
        }
        Object[] newArray = (Object[]) Array.newInstance(objectArray.getClass().getComponentType(), length);
        System.arraycopy(objectArray, startIndex, newArray, 0, length);
        return newArray;
    }
