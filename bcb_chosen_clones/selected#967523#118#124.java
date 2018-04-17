    public static Object[] concatenate(Object[] a0, Object[] a1) {
        Class clazz = a0.getClass().getComponentType();
        Object[] a2 = (Object[]) Array.newInstance(clazz, a0.length + a1.length);
        System.arraycopy(a0, 0, a2, 0, a0.length);
        System.arraycopy(a1, 0, a2, a0.length, a1.length);
        return a2;
    }
