    public static Object getClassInstance(String className, Object arg) throws ApplicationException {
        if (arg == null) {
            return getClassInstance(className);
        }
        try {
            Class klass = getClass(className);
            Class[] ca = new Class[1];
            ca[0] = arg.getClass();
            return klass.getConstructor(ca).newInstance(new Object[] { arg });
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }
