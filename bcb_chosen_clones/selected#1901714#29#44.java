    private static DAO instantiateDAO(String type) throws NoAvailableDAOException {
        DAO dao;
        String dbType = Configuration.getString("dbType").toUpperCase();
        Class c;
        Constructor constructor;
        String fullDAOName = dbType + type + "DAO";
        try {
            c = Class.forName("edu.uga.galileo.voci.db.dao." + fullDAOName);
            constructor = c.getConstructor((Class[]) null);
            dao = (DAO) constructor.newInstance((Object[]) null);
            return dao;
        } catch (Exception e1) {
            Logger.error("Couldn't instantiate '" + fullDAOName + "'", e1);
            throw new NoAvailableDAOException("Couldn't instantiate a " + fullDAOName);
        }
    }
