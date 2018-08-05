    public static EntityManager createEntityManager(SessionFactory sf, PersistenceContextType persistenceContextType, PersistenceUnitTransactionType persistenceUnitTransactionType, boolean TRANSACTION, Properties RESOURCE_LOCAL) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("EntityManager Implementation version: " + EntityManagerImpl.class.getPackage().getImplementationVersion());
        }
        try {
            Constructor<EntityManagerImpl> ct = null;
            Object[] args = null;
            try {
                final Class[] partypes1 = new Class[] { SessionFactory.class, PersistenceContextType.class, PersistenceUnitTransactionType.class, Boolean.TYPE, Map.class };
                ct = EntityManagerImpl.class.getConstructor(partypes1);
                args = new Object[] { sf, persistenceContextType, persistenceUnitTransactionType, TRANSACTION, RESOURCE_LOCAL };
            } catch (Throwable ignored) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Old EntityManagerImpl not detected.");
                }
            }
            if (ct == null) {
                try {
                    final Class[] partypes2 = new Class[] { SessionFactory.class, PersistenceContextType.class, PersistenceUnitTransactionType.class, Boolean.TYPE, Class.class, Map.class };
                    ct = EntityManagerImpl.class.getConstructor(partypes2);
                    args = new Object[] { sf, persistenceContextType, persistenceUnitTransactionType, TRANSACTION, null, RESOURCE_LOCAL };
                } catch (Throwable ex) {
                    throw new RuntimeException("Could not construct new EntityManagerImpl", ex);
                }
            }
            return ct.newInstance(args);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }
