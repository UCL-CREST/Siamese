    public BaseDAO createDAO(Class daoInterface) throws DAOFactoryException {
        BaseDAO dao = null;
        try {
            log.debug("creating DAO for interface " + daoInterface.getName());
            int hashcode = getPersistenceLayerConfig().hashCode();
            if (configHashcode != -1 && hashcode != configHashcode) {
                synchronized (this) {
                    hashcode = getPersistenceLayerConfig().hashCode();
                    if (hashcode != configHashcode) {
                        log.debug("config object has changed, clearing DAO class object cache");
                        daoClassMap.clear();
                        configHashcode = hashcode;
                    }
                }
            }
            Class daoClass = (Class) daoClassMap.get(daoInterface.getName());
            if (daoClass == null) {
                synchronized (this) {
                    daoClass = (Class) daoClassMap.get(daoInterface.getName());
                    if (daoClass == null) {
                        daoClass = loadDAOClass(daoInterface);
                        daoClassMap.put(daoInterface.getName(), daoClass);
                    }
                }
            }
            Constructor constructor = daoClass.getConstructor(new Class[] { PersistenceContext.class });
            dao = (BaseDAO) constructor.newInstance(new Object[] { context });
        } catch (Throwable t) {
            throw new DAOFactoryException(daoInterface, t);
        }
        return dao;
    }
