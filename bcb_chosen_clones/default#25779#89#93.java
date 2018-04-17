    private void doit(String args[]) throws Exception {
        Class clazz = classLoader.loadClass("org.doit.muffin.Main");
        Method method = clazz.getMethod("main", new Class[] { args.getClass() });
        method.invoke(null, new Object[] { args });
    }
