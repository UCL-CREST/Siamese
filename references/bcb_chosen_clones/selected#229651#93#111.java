    public static ArrayDataSet wrap(Object array, int[] qube, boolean copy) {
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
        if (c == double.class) return DDataSet.wrap((double[]) arr, qube);
        if (c == float.class) return FDataSet.wrap((float[]) arr, qube);
        if (c == long.class) return LDataSet.wrap((long[]) arr, qube);
        if (c == int.class) return IDataSet.wrap((int[]) arr, qube);
        if (c == short.class) return SDataSet.wrap((short[]) arr, qube);
        if (c == byte.class) return BDataSet.wrap((byte[]) arr, qube);
        throw new IllegalArgumentException("component type not supported: " + c);
    }
