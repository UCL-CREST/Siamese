    public static Object[] remove(Object[] oldArray, int index) {
        NullArgumentException.check(oldArray);
        if ((index < 0) || (index >= oldArray.length)) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        Object[] newArray = (Object[]) Array.newInstance(oldArray.getClass().getComponentType(), oldArray.length - 1);
        System.arraycopy(oldArray, 0, newArray, 0, index);
        System.arraycopy(oldArray, index + 1, newArray, index, newArray.length - index);
        return newArray;
    }
