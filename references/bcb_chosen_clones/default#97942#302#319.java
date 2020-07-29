    public static Object newInstance(Class o_clazz, Object[] args, Class[] clazzes) throws Throwable {
        boolean[] is_null = new boolean[args.length];
        for (int i = 0; i < args.length; i++) {
            is_null[i] = (args[i] == null);
        }
        Constructor cons = getConstructor(o_clazz, clazzes, is_null);
        boolean access = cons.isAccessible();
        cons.setAccessible(true);
        Object o;
        try {
            o = cons.newInstance(args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } finally {
            cons.setAccessible(access);
        }
        return o;
    }
