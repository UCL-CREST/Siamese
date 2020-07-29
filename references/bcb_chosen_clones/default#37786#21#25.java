    static void javac(String... args) throws Exception {
        Class c = Class.forName("com.sun.tools.javac.Main", true, cl);
        int status = (Integer) c.getMethod("compile", new Class[] { String[].class }).invoke(c.newInstance(), new Object[] { args });
        if (status != 0) throw new Exception("javac failed: status=" + status);
    }
