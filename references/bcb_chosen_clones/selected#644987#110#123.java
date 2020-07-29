    public static Object[] remove(Object[] array, Object obj) {
        int index = indexOf(array, obj);
        if (index < 0) {
            return array;
        }
        Object[] newArray = (Object[]) Array.newInstance(array.getClass().getComponentType(), array.length - 1);
        if (index > 0) {
            System.arraycopy(array, 0, newArray, 0, index);
        }
        if (index < array.length - 1) {
            System.arraycopy(array, index + 1, newArray, index, newArray.length - index);
        }
        return newArray;
    }
