    public static void main(String[] args) {
        Install ld = new Install();
        try {
            uhfc = ld.loadClass("net.sourceforge.liftoff.installer.source.ResURLFactory");
            uhf = (URLStreamHandlerFactory) uhfc.newInstance();
            URL.setURLStreamHandlerFactory(uhf);
        } catch (Exception e) {
            if (e instanceof ClassNotFoundException) {
                System.err.println("can not load class  net.sourceforge.liftoff.installer.source.ResURLFactory :" + e);
                System.exit(1);
            }
        }
        try {
            Class cl = ld.loadClass(classToRun, true);
            Class[] margsClasses = { args.getClass() };
            Object[] margs = { args };
            Method m = cl.getMethod("main", margsClasses);
            m.invoke(null, margs);
        } catch (Exception e) {
            if (e instanceof ClassNotFoundException) {
                System.err.println("can not load class " + classToRun + " : " + e);
                System.exit(1);
            }
            if (e instanceof NoSuchMethodException) {
                System.err.println("can not find method main in " + classToRun + " : " + e);
                System.exit(1);
            }
            if (e instanceof InvocationTargetException) {
                System.err.println("exception in called method " + classToRun + ".main");
                Throwable tr = ((InvocationTargetException) e).getTargetException();
                tr.printStackTrace();
            }
            System.err.println(e);
            System.exit(1);
        }
    }
