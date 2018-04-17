    private static ClassLoader getDexClassLoader() {
        String odexDir;
        File test = new File(ODEX_DIR);
        if (test.isDirectory()) odexDir = ODEX_DIR; else odexDir = ODEX_ALT;
        ClassLoader myLoader = Main.class.getClassLoader();
        Class dclClass;
        try {
            dclClass = myLoader.loadClass("dalvik.system.DexClassLoader");
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException("dalvik.system.DexClassLoader not found");
        }
        Constructor ctor;
        try {
            ctor = dclClass.getConstructor(String.class, String.class, String.class, ClassLoader.class);
        } catch (NoSuchMethodException nsme) {
            throw new RuntimeException("DCL ctor", nsme);
        }
        Object dclObj;
        try {
            dclObj = ctor.newInstance(CLASS_PATH, odexDir, LIB_DIR, myLoader);
        } catch (Exception ex) {
            throw new RuntimeException("DCL newInstance", ex);
        }
        return (ClassLoader) dclObj;
    }
