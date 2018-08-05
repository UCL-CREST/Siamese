    public static Object concatArrays(Object[] a, Object[] b) {
        if (a == null) return b;
        if (b == null) return a;
        Object result = Array.newInstance(a.getClass().getComponentType(), a.length + b.length);
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
