    public Object call(Object[] args) throws Throwable {
        try {
            return ctor.getConstructor().newInstance(args);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw Utils.wrapInvocationException(e);
        }
    }
