    private static HashMap getProcessEnvironment() {
        HashMap defaultEnv = new HashMap();
        String val = null;
        if (winnt.isDirectory()) val = "c:\\winnt"; else if (windows.isDirectory()) val = "c:\\windows";
        try {
            String s = System.getenv("SystemRoot");
            if (s != null) val = s;
        } catch (Throwable t) {
        }
        try {
            String s = System.getProperty("Windows.SystemRoot");
            if (s != null) val = s;
        } catch (Throwable t) {
        }
        if (val != null) defaultEnv.put("SystemRoot", val);
        try {
            Method m = System.class.getMethod("getenv", EMPTY_PARAM);
            Map map = (Map) m.invoke(System.class, EMPTY_ARG);
            defaultEnv.putAll(map);
        } catch (Exception e) {
            defaultEnv.putAll(COMMON_ENVIRONMENT);
        }
        return defaultEnv;
    }
