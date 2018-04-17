    private static HashMap getCommonEnvironment() {
        String entries[] = { "PATH", "LD_LIBRARY_PATH", "LD_ASSUME_KERNEL", "USER", "TMP", "TEMP", "HOME", "HOMEPATH", "LANG", "TZ", "OS" };
        HashMap map = new HashMap(entries.length + 10);
        String val;
        Method m = null;
        try {
            m = System.class.getMethod("getenv", STRING_PARAM);
        } catch (Exception e) {
        }
        for (int i = 0; i < entries.length; i++) {
            val = null;
            if (m != null) {
                try {
                    val = (String) m.invoke(System.class, (Object[]) new String[] { entries[i] });
                } catch (Exception e) {
                    m = null;
                }
            }
            if (val == null) {
                try {
                    val = System.getProperty(entries[i]);
                } catch (Exception e) {
                }
            }
            if (val != null) map.put(entries[i], val);
        }
        return map;
    }
