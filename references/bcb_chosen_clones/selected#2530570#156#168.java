    public static Object[] insert(Object[] array, int index, int number) {
        if ((index < 0) || (index > array.length)) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        Object[] tmp = (Object[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), array.length + number);
        if (index > 0) {
            System.arraycopy(array, 0, tmp, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, tmp, index + number, array.length - index);
        }
        return tmp;
    }
