    public static PO FactoryPO(Class<?> c, Properties ctx, int id, String trx) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> con = c.getConstructor(Properties.class, int.class, String.class);
        PO p = (PO) con.newInstance(ctx, id, trx);
        return p;
    }
