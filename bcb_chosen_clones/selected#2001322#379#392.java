    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] contents) {
        int size = size();
        if (size > contents.length) {
            Class<?> ct = contents.getClass().getComponentType();
            contents = (T[]) Array.newInstance(ct, size);
        }
        System.arraycopy(array, firstIndex, contents, 0, size);
        if (size < contents.length) {
            contents[size] = null;
        }
        return contents;
    }
