    static DaoException createException(Class<?> clazz, Exception e) throws Exception {
        Constructor<?> c = clazz.getConstructor(Exception.class);
        DaoException ret = (DaoException) c.newInstance(e);
        return ret;
    }
