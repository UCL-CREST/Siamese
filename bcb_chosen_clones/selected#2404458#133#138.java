    static Object concatArrays(Object a, Object b) {
        Object[] result = (Object[]) Array.newInstance(a.getClass().getComponentType(), Array.getLength(a) + Array.getLength(b));
        System.arraycopy(a, 0, result, 0, Array.getLength(a));
        System.arraycopy(b, 0, result, Array.getLength(a), Array.getLength(b));
        return result;
    }
