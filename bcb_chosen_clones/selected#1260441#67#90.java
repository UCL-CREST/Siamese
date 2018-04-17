    protected TransactionManager getTransactionManagerForLocator(ClassLoaderResolver clr, String locatorClassName) {
        try {
            Class cls = clr.classForName(locatorClassName);
            if (cls != null) {
                TransactionManagerLocator loc = null;
                try {
                    Class[] params = new Class[] { ClassConstants.NUCLEUS_CONTEXT };
                    Constructor ctor = cls.getConstructor(params);
                    Object[] args = new Object[] { nucleusContext };
                    loc = (TransactionManagerLocator) ctor.newInstance(args);
                } catch (NoSuchMethodException nsme) {
                    loc = (TransactionManagerLocator) cls.newInstance();
                }
                if (loc != null) {
                    TransactionManager tm = loc.getTransactionManager(clr);
                    if (tm != null) {
                        return tm;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
