    public <T> T instantiate(Class<T> type, Object... args) {
        Class[] paramType = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            paramType[i] = args[i].getClass();
        }
        try {
            return type.getConstructor(paramType).newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Impossible to instantiate object of type " + type.getName(), e);
        }
    }
