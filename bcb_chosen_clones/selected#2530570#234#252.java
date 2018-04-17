    public static Object[] remove(Object[] array, int index, int number) {
        if ((index < 0) || (index >= array.length)) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        if ((array.length - index) < number) {
            number = array.length - index;
        }
        if ((index == 0) && (array.length == number)) {
            return null;
        }
        Object[] tmp = (Object[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), array.length - number);
        if (index > 0) {
            System.arraycopy(array, 0, tmp, 0, index);
        }
        if (index + number < array.length) {
            System.arraycopy(array, index + number, tmp, index, array.length - index - number);
        }
        return tmp;
    }
