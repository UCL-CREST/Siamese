    protected boolean loadJarLibrary(final String jarLib) {
        final String tempLib = System.getProperty("java.io.tmpdir") + File.separator + jarLib;
        boolean copied = IOUtils.copyFile(jarLib, tempLib);
        if (!copied) {
            return false;
        }
        System.load(tempLib);
        return true;
    }
