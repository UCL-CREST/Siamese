    @Override
    public <T> T[] toArray(T[] array) {
        if (size > 0) {
            if (array.length < size) {
                @SuppressWarnings("unchecked") T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), size);
                array = newArray;
            }
            System.arraycopy(data, 0, array, 0, size);
        }
        if (array.length > size) {
            array[size] = null;
        }
        return array;
    }
