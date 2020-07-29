    public static int[] openKey1(int hkey, byte[] windowsAbsolutePath, int securityMask) throws Exception {
        Class theClass = Class.forName("java.util.prefs.WindowsPreferences");
        Method m = theClass.getDeclaredMethod("WindowsRegOpenKey", new Class[] { int.class, byte[].class, int.class });
        m.setAccessible(true);
        Object ret = m.invoke(null, new Object[] { new Integer(hkey), windowsAbsolutePath, new Integer(securityMask) });
        return (int[]) ret;
    }
