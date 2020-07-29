    void run(String className, String[] args, boolean background) {
        if (!background) {
            try {
                Class cls = Class.forName(className);
                Class[] argClses = { String[].class };
                Method mainMethod = cls.getMethod("main", argClses);
                Object[] argObjs = { args };
                mainMethod.invoke(null, argObjs);
            } catch (InvocationTargetException ite) {
                ite.getTargetException().printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                BackgroundThread t = new BackgroundThread(this, className, args);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
