    private static <T> T construct(Class<? extends T> clazz, Class<?>[] argTypes, Object[] args) throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        @SuppressWarnings("unchecked") Constructor<T>[] ctcs = (Constructor<T>[]) clazz.getDeclaredConstructors();
        for (Constructor<T> ctc : ctcs) {
            Class<?>[] paramTypes = ctc.getParameterTypes();
            if (paramsValid(paramTypes, argTypes)) {
                ctc.setAccessible(true);
                return ctc.newInstance(args);
            }
        }
        return clazz.getConstructor(argTypes).newInstance(args);
    }
