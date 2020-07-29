    public static TrArrayDataSet wrap(Object array, int[] qube, boolean copy) {
        Object arr;
        if (!array.getClass().isArray()) throw new IllegalArgumentException("input must be an array");
        Class c = array.getClass().getComponentType();
        if (c.isArray()) throw new IllegalArgumentException("input must be 1-D array");
        if (copy) {
            arr = Array.newInstance(c, Array.getLength(array));
            System.arraycopy(array, 0, arr, 0, Array.getLength(array));
        } else {
            arr = array;
        }
        if (c == double.class) return TrDDataSet.wrap((double[]) array, qube);
        if (c == float.class) return TrFDataSet.wrap((float[]) array, qube);
        if (c == long.class) return TrLDataSet.wrap((long[]) array, qube);
        throw new IllegalArgumentException("component type not supported: " + c);
    }
