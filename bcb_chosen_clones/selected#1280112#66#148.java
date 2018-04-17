    EmbeddedFrame createEmbeddedFrame(long window) {
        EmbeddedFrame ef = null;
        String version = System.getProperty("java.version");
        String os = System.getProperty("os.name");
        if ((version.indexOf("1.5") == -1) || (os.equals("SunOS"))) {
            long w = getWidget(window, 400, 400, 0, 0);
            Class clazz = null;
            try {
                clazz = Class.forName("sun.awt.motif.MEmbeddedFrame");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Constructor constructor = null;
            try {
                constructor = clazz.getConstructor(new Class[] { int.class });
            } catch (Throwable e1) {
                try {
                    constructor = clazz.getConstructor(new Class[] { long.class });
                } catch (Throwable e2) {
                    e1.printStackTrace();
                }
            }
            Object value = null;
            try {
                value = constructor.newInstance(new Object[] { new Long(w) });
            } catch (Throwable e) {
                e.printStackTrace();
            }
            ef = (EmbeddedFrame) value;
        } else {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            if (toolkit instanceof sun.awt.motif.MToolkit) {
                Class clazz = null;
                try {
                    clazz = Class.forName("sun.awt.motif.MEmbeddedFrame");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                Constructor constructor = null;
                try {
                    constructor = clazz.getConstructor(new Class[] { int.class });
                } catch (Throwable e1) {
                    try {
                        constructor = clazz.getConstructor(new Class[] { long.class });
                    } catch (Throwable e2) {
                        e1.printStackTrace();
                    }
                }
                Object value = null;
                try {
                    value = constructor.newInstance(new Object[] { new Long(window) });
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                ef = (EmbeddedFrame) value;
            } else {
                Class clazz = null;
                try {
                    clazz = Class.forName("sun.awt.X11.XEmbeddedFrame");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                Constructor constructor = null;
                try {
                    constructor = clazz.getConstructor(new Class[] { int.class });
                } catch (Throwable e1) {
                    try {
                        constructor = clazz.getConstructor(new Class[] { long.class });
                    } catch (Throwable e2) {
                        e1.printStackTrace();
                    }
                }
                Object value = null;
                try {
                    value = constructor.newInstance(new Object[] { new Long(window) });
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                ef = (EmbeddedFrame) value;
            }
        }
        return ef;
    }
