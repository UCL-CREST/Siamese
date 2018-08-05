    public static final Object[] addToArray(Object[] _array, Object _item) {
        if (_array == null) {
            Class cla = _item.getClass();
            Object[] array = (Object[]) Array.newInstance(cla, 1);
            array[0] = _item;
            return array;
        }
        Class cla = _array.getClass().getComponentType();
        Object[] array = (Object[]) Array.newInstance(cla, Array.getLength(_array) + 1);
        System.arraycopy(_array, 0, array, 0, _array.length);
        array[_array.length] = _item;
        return array;
    }
