    public static void main(String[] args) {
        try {
            cd(Run.class);
            Manifest mf = getManifest();
            URL[] classpath = getClasspath(mf);
            ClassLoader cl = new URLClassLoader(classpath);
            Thread.currentThread().setContextClassLoader(cl);
            Class clazz = cl.loadClass(getClass(mf));
            Method method = clazz.getMethod("main", new Class[] { String[].class });
            method.invoke(null, new Object[] { args });
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
