    public static void main(String[] args) {
        String className = "sce.swt.SceneryConfigEditor";
        File workingDirectory = getWorkingDirectory();
        String arch = System.getProperty("os.arch");
        int archCode = (arch.indexOf("64") >= 0) ? 64 : 32;
        ArrayList<URL> urls = new ArrayList<URL>();
        File archDir = new File(workingDirectory, "lib" + archCode);
        dirToURLs(archDir, urls);
        File libDir = new File(workingDirectory, "lib");
        dirToURLs(libDir, urls);
        if (urls.size() > 0) {
            ClassLoader tcl = Thread.currentThread().getContextClassLoader();
            URLClassLoader urlClassLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), tcl);
            Thread.currentThread().setContextClassLoader(urlClassLoader);
        }
        ClassLoader tcl = Thread.currentThread().getContextClassLoader();
        try {
            Class<?> clazz = tcl.loadClass(className);
            Method main = clazz.getMethod("main", String[].class);
            main.getClass().getClassLoader();
            main.invoke(null, (Object) args);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
