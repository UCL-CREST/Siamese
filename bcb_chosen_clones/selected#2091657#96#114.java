    public static final Object[] removeFromArray(Object[] _array, Object _item) {
        if (_item == null || _array == null) {
            return _array;
        }
        for (int i = _array.length; i-- > 0; ) {
            if (_item.equals(_array[i])) {
                Class cla = _array == null ? _item.getClass() : _array.getClass().getComponentType();
                Object[] array = (Object[]) Array.newInstance(cla, Array.getLength(_array) - 1);
                if (i > 0) {
                    System.arraycopy(_array, 0, array, 0, i);
                }
                if (i + 1 < _array.length) {
                    System.arraycopy(_array, i + 1, array, i, _array.length - (i + 1));
                }
                return array;
            }
        }
        return _array;
    }
