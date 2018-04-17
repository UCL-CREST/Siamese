    public static Object[] remove(Object[] array, int index) {
        if ((index < 0) || (index >= array.length)) throw new ArrayIndexOutOfBoundsException(index);
        if (array.length == 1) return null;
        Object[] tmp = (Object[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), array.length - 1);
        if (index > 0) {
            System.arraycopy(array, 0, tmp, 0, index);
        }
        if (index < tmp.length) {
            System.arraycopy(array, index + 1, tmp, index, tmp.length - index);
        }
        return tmp;
    }
