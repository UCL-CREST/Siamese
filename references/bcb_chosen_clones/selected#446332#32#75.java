    public JFrame getJFrame(Object roleObject) {
        if (!(roleObject instanceof ServiceItem)) {
            throw new IllegalArgumentException("ServiceItem required");
        }
        ClassLoader cl = ((ServiceItem) roleObject).service.getClass().getClassLoader();
        JFrame component = null;
        final URLClassLoader uiLoader = URLClassLoader.newInstance(exportURL, cl);
        final Thread currentThread = Thread.currentThread();
        final ClassLoader parentLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                return (currentThread.getContextClassLoader());
            }
        });
        try {
            AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {
                    currentThread.setContextClassLoader(uiLoader);
                    return (null);
                }
            });
            try {
                Class clazz = uiLoader.loadClass(className);
                Constructor constructor = clazz.getConstructor(new Class[] { Object.class });
                Object instanceObj = constructor.newInstance(new Object[] { roleObject });
                component = (JFrame) instanceObj;
            } catch (Throwable t) {
                if (t.getCause() != null) t = t.getCause();
                IllegalArgumentException e = new IllegalArgumentException("Unable to instantiate ServiceUI :" + t.getClass().getName() + ": " + t.getLocalizedMessage());
                e.initCause(t);
                throw e;
            }
        } finally {
            AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {
                    currentThread.setContextClassLoader(parentLoader);
                    return (null);
                }
            });
        }
        return (component);
    }
