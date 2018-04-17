    public static String getValue(int hkey, byte[] WINDOWS_ROOT_PATH, String key) throws Exception {
        Class theClass = Class.forName("java.util.prefs.WindowsPreferences");
        byte[] windowsName;
        int nativeHandle;
        Object value;
        int[] result;
        Method m;
        result = openKey1(hkey, windowsAbsolutePath(WINDOWS_ROOT_PATH), KEY_QUERY_VALUE);
        if (result[ERROR_CODE] != ERROR_SUCCESS) {
            throw new Exception("Path not found!");
        }
        nativeHandle = result[NATIVE_HANDLE];
        m = theClass.getDeclaredMethod("WindowsRegQueryValueEx", new Class[] { int.class, byte[].class });
        m.setAccessible(true);
        windowsName = toWindowsName(key);
        value = m.invoke(null, new Object[] { new Integer(nativeHandle), windowsName });
        WindowsRegCloseKey(nativeHandle);
        if (value == null) {
            throw new Exception("Path found.  Key not found.");
        }
        byte[] origBuffer = (byte[]) value;
        if (origBuffer.length == 0) return null;
        byte[] destBuffer = new byte[origBuffer.length - 1];
        System.arraycopy(origBuffer, 0, destBuffer, 0, origBuffer.length - 1);
        return new String(destBuffer);
    }
