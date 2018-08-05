    private static CollectionPersister create(Class persisterClass, Configuration cfg, Collection model, CollectionRegionAccessStrategy cacheAccessStrategy, SessionFactoryImplementor factory) throws HibernateException {
        Constructor pc;
        try {
            pc = persisterClass.getConstructor(COLLECTION_PERSISTER_CONSTRUCTOR_ARGS);
        } catch (Exception e) {
            throw new MappingException("Could not get constructor for " + persisterClass.getName(), e);
        }
        try {
            return (CollectionPersister) pc.newInstance(new Object[] { model, cacheAccessStrategy, cfg, factory });
        } catch (InvocationTargetException ite) {
            Throwable e = ite.getTargetException();
            if (e instanceof HibernateException) {
                throw (HibernateException) e;
            } else {
                throw new MappingException("Could not instantiate collection persister " + persisterClass.getName(), e);
            }
        } catch (Exception e) {
            throw new MappingException("Could not instantiate collection persister " + persisterClass.getName(), e);
        }
    }
