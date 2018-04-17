    public static IKoratArray createArray(Class<?> koratArrayClz, int size) throws Exception {
        Constructor c = koratArrayClz.getConstructor(new Class[] { int.class });
        IKoratArray ret = (IKoratArray) c.newInstance(new Object[] { size });
        return ret;
    }
