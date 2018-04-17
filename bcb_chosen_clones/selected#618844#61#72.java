    public static <T> T[] addToArray(T[] array, T object) {
        Object[] newArray = null;
        if (array == null) {
            newArray = (Object[]) Array.newInstance(object.getClass(), 1);
            newArray[0] = object;
        } else {
            newArray = (Object[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
            System.arraycopy(array, 0, newArray, 0, array.length);
            newArray[array.length] = object;
        }
        return (T[]) newArray;
    }
