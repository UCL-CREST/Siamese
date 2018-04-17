    private static void signApk(File apk) throws Exception {
        URLClassLoader loader = URLClassLoader.newInstance(new URL[] { new URL(testsign) });
        Class clazz = loader.loadClass("testsign");
        Method method = clazz.getMethod("main", String[].class);
        method.setAccessible(true);
        method.invoke(null, (Object) new String[] { apk.getPath() });
    }
