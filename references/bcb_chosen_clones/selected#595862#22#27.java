    public static Object[] add(final Object[] array, final Object obj) {
        final Object[] newArray = (Object[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = obj;
        return newArray;
    }
