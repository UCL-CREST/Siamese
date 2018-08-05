    protected boolean internalCompile(String[] args) {
        System.out.println("Args: ");
        for (String arg : args) {
            System.out.print(arg + " ");
        }
        System.out.println();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class comSunToolsJavacMainClass = null;
        try {
            comSunToolsJavacMainClass = cl.loadClass("pl.wcislo.sbql4j.tools.javac.Main");
            try {
                Method compileMethod = comSunToolsJavacMainClass.getMethod("compile", compile141MethodSignature);
                try {
                    Object result = compileMethod.invoke(null, new Object[] { args, new PrintWriter(out) });
                    if (!(result instanceof Integer)) {
                        return false;
                    }
                    return ((Integer) result).intValue() == 0;
                } catch (IllegalAccessException e3) {
                    return false;
                } catch (IllegalArgumentException e3) {
                    return false;
                } catch (InvocationTargetException e3) {
                    return false;
                }
            } catch (NoSuchMethodException e2) {
                System.out.println("ERROR: Compile failed with error:" + e2.toString());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }
