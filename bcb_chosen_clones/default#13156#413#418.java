    public void bootClass(String cName, String mName, String[] args) throws java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException, java.lang.NoSuchMethodException, java.lang.ClassNotFoundException {
        Class c = findClass(cName);
        resolveClass(c);
        java.lang.reflect.Method m = c.getMethod(mName, new Class[] { String[].class });
        m.invoke(null, new Object[] { args });
    }
