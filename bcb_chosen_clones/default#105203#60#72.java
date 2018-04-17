    void callValues() {
        try {
            File dot = new File(System.getProperty("user.dir"));
            ClassLoader cl = new URLClassLoader(new URL[] { dot.toURL() });
            Class<?> e_class = cl.loadClass("E");
            Method m = e_class.getMethod("values", new Class[] {});
            Object o = m.invoke(null, (Object[]) null);
            List<Object> v = Arrays.asList((Object[]) o);
            if (!v.toString().equals("[a, b, c]")) throw new Error("unexpected result for E.values(): " + v);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
