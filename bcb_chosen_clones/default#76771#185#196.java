    public static void callVoidMethod(Class<?> clz, String methodName, String[] args) {
        Class<?>[] arg = new Class<?>[1];
        arg[0] = args.getClass();
        try {
            Method method = clz.getMethod(methodName, arg);
            Object[] inArg = new Object[1];
            inArg[0] = args;
            method.invoke(clz, inArg);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
