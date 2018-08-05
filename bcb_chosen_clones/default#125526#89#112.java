    static void launch(String launchClassName, String[] args) {
        URL[] classpathURLs = _classpath.toArray(new URL[_classpath.size()]);
        try {
            URLClassLoader newLoader = new URLClassLoader(classpathURLs, Thread.currentThread().getContextClassLoader());
            Thread.currentThread().setContextClassLoader(newLoader);
            Class<?> launchClass = newLoader.loadClass(launchClassName);
            SystemPropertiesReader sysPropReader = new SystemPropertiesReader(newLoader);
            sysPropReader.readSystemProperties();
            Method main = launchClass.getMethod("main", new Class[] { String[].class });
            main.invoke(null, new Object[] { args });
        } catch (ClassNotFoundException ex) {
            fail(String.format("Class '%s' not found.", launchClassName));
        } catch (NoSuchMethodException ex) {
            fail(String.format("Class '%s' does not contain a main() method.", launchClassName));
        } catch (Exception ex) {
            Throwable root = ex;
            Throwable cause = null;
            while ((cause = root.getCause()) != null) {
                root = cause;
            }
            root.printStackTrace();
            fail(String.format("Error invoking method main() of %s: %s", launchClassName, ex.toString()));
        }
    }
