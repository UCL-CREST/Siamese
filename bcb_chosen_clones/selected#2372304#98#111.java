    public static Object cut(Object src, int position) {
        int size = Array.getLength(src);
        if (size == 1) {
            return Array.newInstance(src.getClass().getComponentType(), 0);
        }
        int numMoved = size - position - 1;
        if (numMoved > 0) {
            System.arraycopy(src, position + 1, src, position, numMoved);
        }
        size--;
        Object dest = Array.newInstance(src.getClass().getComponentType(), size);
        System.arraycopy(src, 0, dest, 0, size);
        return dest;
    }
