    public static final Object[] insert(Object[] array, Object o, int index) {
        Object[] tArray = (Object[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, tArray, 0, array.length);
        System.arraycopy(tArray, index, tArray, index + 1, array.length - index);
        tArray[index] = o;
        return tArray;
    }
