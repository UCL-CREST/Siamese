    public static int WindowsRegCloseKey(int nativeHandle) throws Exception {
        Class theClass = Class.forName("java.util.prefs.WindowsPreferences");
        Method m = theClass.getDeclaredMethod("WindowsRegCloseKey", new Class[] { int.class });
        Object ret;
        m.setAccessible(true);
        ret = m.invoke(null, new Object[] { new Integer(nativeHandle) });
        return ((Integer) ret).intValue();
    }
