    private static EntityPersister create(Class persisterClass, PersistentClass model, EntityRegionAccessStrategy cacheAccessStrategy, SessionFactoryImplementor factory, Mapping cfg) throws HibernateException {
        Constructor pc;
        try {
            pc = persisterClass.getConstructor(PERSISTER_CONSTRUCTOR_ARGS);
        } catch (Exception e) {
            throw new MappingException("Could not get constructor for " + persisterClass.getName(), e);
        }
        try {
            return (EntityPersister) pc.newInstance(new Object[] { model, cacheAccessStrategy, factory, cfg });
        } catch (InvocationTargetException ite) {
            Throwable e = ite.getTargetException();
            if (e instanceof HibernateException) {
                throw (HibernateException) e;
            } else {
                throw new MappingException("Could not instantiate persister " + persisterClass.getName(), e);
            }
        } catch (Exception e) {
            throw new MappingException("Could not instantiate persister " + persisterClass.getName(), e);
        }
    }
