    public static AbstractONDEXPlugin getPlugin(String str) {
        AbstractONDEXPlugin p = null;
        try {
            Class<?> cls = Class.forName(str);
            p = (AbstractONDEXPlugin) cls.getConstructor(new Class<?>[] {}).newInstance();
        } catch (Exception e) {
        }
        return p;
    }
