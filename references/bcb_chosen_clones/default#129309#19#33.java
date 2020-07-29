    public void startApp(String mainClassName, String mainArgs[]) {
        try {
            File path = new File("./");
            sun.misc.CDCAppClassLoader loader = new CDCAppClassLoader(new URL[] { path.toURL() }, null);
            Class[] args1 = { new String[0].getClass() };
            Object[] args2 = { mainArgs };
            Class mainClass = loader.loadClass(mainClassName);
            Method mainMethod = mainClass.getMethod("main", args1);
            mainMethod.invoke(null, args2);
        } catch (InvocationTargetException i) {
            i.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
