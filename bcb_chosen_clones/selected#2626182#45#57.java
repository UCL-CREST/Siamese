    @SuppressWarnings("unchecked")
    @Override
    public <E> E[] toArray(final E[] array) {
        if (array.length < _array.length()) {
            final E[] copy = (E[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), _array.length());
            for (int i = 0; i < _array.length(); ++i) {
                copy[i] = (E) _array.get(i);
            }
            return copy;
        }
        System.arraycopy(_array._array, _array._start, array, 0, array.length);
        return array;
    }
