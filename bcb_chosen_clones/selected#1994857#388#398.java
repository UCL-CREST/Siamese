    @SuppressWarnings(UNCHECKED)
    public <T> T[] toArray(final T[] elements) {
        final T[] toArray;
        if (elements.length >= size) {
            toArray = elements;
        } else {
            toArray = (T[]) java.lang.reflect.Array.newInstance(elements.getClass().getComponentType(), size);
        }
        System.arraycopy(array, 0, toArray, 0, size);
        return toArray;
    }
