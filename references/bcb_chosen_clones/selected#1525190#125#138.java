    public static Object cut(Object obj, int size) {
        int j;
        if ((j = Array.getLength(obj)) == 1) {
            return Array.newInstance(obj.getClass().getComponentType(), 0);
        }
        int k;
        if ((k = j - size - 1) > 0) {
            System.arraycopy(obj, size + 1, obj, size, k);
        }
        j--;
        Object obj1 = Array.newInstance(obj.getClass().getComponentType(), j);
        System.arraycopy(obj, 0, obj1, 0, j);
        return obj1;
    }
