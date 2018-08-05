    private static URL getClassURL() {
        ClassLoader cl = WebstartUtil.class.getClassLoader();
        Method m;
        try {
            m = cl.getClass().getMethod("findResource", new Class[] { String.class });
            if (!m.getReturnType().equals(URL.class)) return null;
            String name = WebstartUtil.class.getName();
            String path = name.replace('.', '/').concat(".class");
            return (URL) m.invoke(cl, new Object[] { path });
        } catch (Throwable t) {
            return null;
        }
    }
