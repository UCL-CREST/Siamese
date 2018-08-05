    public static Object[] addToArray(Object[] array, Object member) {
        Object[] newArray = (Object[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = member;
        return newArray;
    }
