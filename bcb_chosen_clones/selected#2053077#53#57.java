    public static PO FactoryPO(Class<?> c, Properties ctx, ResultSet rs, String trx) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> con = c.getConstructor(Properties.class, ResultSet.class, String.class);
        PO p = (PO) con.newInstance(ctx, rs, trx);
        return p;
    }
