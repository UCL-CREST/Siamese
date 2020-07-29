    public static Object getInstance(String className, Object[] parameters) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class[] sig = new Class[parameters.length];
        for (int i = 0; i < sig.length; i++) {
            sig[i] = parameters[i].getClass();
        }
        Class c = loadClass(className);
        Constructor con = c.getConstructor(sig);
        Object o = con.newInstance(parameters);
        if (Debug.verboseOn()) Debug.logVerbose("Instantiated object: " + o.toString(), module);
        return o;
    }
