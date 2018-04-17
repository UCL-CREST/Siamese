    public static Object newInstance(Class clazz, Class[] types, Object[] initargs) {
        try {
            Constructor cr = clazz.getConstructor(types);
            return cr.newInstance(initargs);
        } catch (Exception e) {
            return null;
        }
    }
