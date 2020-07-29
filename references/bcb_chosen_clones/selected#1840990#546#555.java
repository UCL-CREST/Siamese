    public <T> T[] toArray(T[] array) {
        int l;
        if ((l = array.length) > size) {
            array[size] = null;
        } else if (l < size) {
            array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        }
        arraycopy(elements, 0, array, 0, size);
        return array;
    }
