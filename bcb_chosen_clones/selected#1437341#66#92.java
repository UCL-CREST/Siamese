    public static Object newInstance(String name, Class param[], Object args[], boolean logException) {
        if (param == null && args != null) {
            param = new Class[args.length];
            for (int i = 0; i < param.length; i++) {
                if (args[i] != null) {
                    param[i] = args[i].getClass();
                } else {
                    param[i] = null;
                }
            }
        }
        Constructor c = null;
        Object obj = null;
        try {
            c = Class.forName(name).getConstructor(param);
            if (c != null) {
                obj = c.newInstance(args);
            }
        } catch (Exception x) {
            if (logException) {
                Logger log = Logger.getLogger(ClassUtil.class);
                log.error("", x);
            }
            return null;
        }
        return obj;
    }
