    public Object toArray() {
        final int length = Array.getLength(storage);
        final Object dst = Array.newInstance(getComponentType(), length);
        System.arraycopy(storage, 0, dst, 0, length);
        return dst;
    }
