    public static final Object[] remove(Object[] array, int index) {
        Object[] tArray = (Object[]) Array.newInstance(array.getClass().getComponentType(), array.length - 1);
        System.arraycopy(array, 0, tArray, 0, index);
        System.arraycopy(array, index + 1, tArray, index, array.length - index - 1);
        return tArray;
    }
