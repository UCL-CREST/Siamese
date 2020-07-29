    public static void checkType() {
        Method m;
        try {
            m = Collections.class.getDeclaredMethod("checkType", Object.class, Class.class);
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
            return;
        }
        m.setAccessible(true);
        try {
            m.invoke(null, new Object(), Object.class);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            return;
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
            return;
        }
        try {
            System.out.println("checkType invoking null");
            m.invoke(null, new Object(), int.class);
            System.out.println("ERROR: should throw InvocationTargetException");
        } catch (InvocationTargetException ite) {
            System.out.println("checkType got expected exception");
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            return;
        }
    }
