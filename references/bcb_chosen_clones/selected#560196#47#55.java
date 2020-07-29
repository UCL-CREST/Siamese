    public Object[] getValues(Object[] array) {
        if (array == null) {
            array = new Object[size];
        } else if (array.length < size) {
            array = (Object[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        }
        System.arraycopy(values, 0, array, 0, size);
        return array;
    }
