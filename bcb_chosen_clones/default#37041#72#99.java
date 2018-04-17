    public static void main(String[] args) {
        try {
            URL coreURL = LauncherBootstrap.class.getResource("/" + LauncherBootstrap.LAUNCHER_JAR_FILE_NAME);
            if (coreURL == null) throw new FileNotFoundException(LauncherBootstrap.LAUNCHER_JAR_FILE_NAME);
            File coreDir = new File(URLDecoder.decode(coreURL.getFile())).getCanonicalFile().getParentFile();
            File propsFile = new File(coreDir, LauncherBootstrap.LAUNCHER_PROPS_FILE_NAME);
            if (!propsFile.canRead()) throw new FileNotFoundException(propsFile.getPath());
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(propsFile);
            props.load(fis);
            fis.close();
            URL[] antURLs = LauncherBootstrap.fileListToURLs((String) props.get(LauncherBootstrap.ANT_CLASSPATH_PROP_NAME));
            URL[] urls = new URL[1 + antURLs.length];
            urls[0] = coreURL;
            for (int i = 0; i < antURLs.length; i++) urls[i + 1] = antURLs[i];
            ClassLoader parentLoader = Thread.currentThread().getContextClassLoader();
            URLClassLoader loader = null;
            if (parentLoader != null) loader = new URLClassLoader(urls, parentLoader); else loader = new URLClassLoader(urls);
            launcherClass = loader.loadClass(LAUNCHER_MAIN_CLASS_NAME);
            Method getLocalizedStringMethod = launcherClass.getDeclaredMethod("getLocalizedString", new Class[] { String.class });
            Method startMethod = launcherClass.getDeclaredMethod("start", new Class[] { String[].class });
            int returnValue = ((Integer) startMethod.invoke(null, new Object[] { args })).intValue();
            System.exit(returnValue);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
