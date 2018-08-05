    private static int ejecutaUpdate(String database, String SQL) throws Exception {
        int i = 0;
        DBConnectionManager dbm = null;
        Connection bd = null;
        try {
            dbm = DBConnectionManager.getInstance();
            bd = dbm.getConnection(database);
            Statement st = bd.createStatement();
            i = st.executeUpdate(SQL);
            bd.commit();
            st.close();
            dbm.freeConnection(database, bd);
        } catch (Exception e) {
            log.error("SQL error: " + SQL, e);
            Exception excep;
            if (dbm == null) excep = new Exception("Could not obtain pool object DbConnectionManager"); else if (bd == null) excep = new Exception("The Db connection pool could not obtain a database connection"); else {
                bd.rollback();
                excep = new Exception("SQL Error: " + SQL + " error: " + e);
                dbm.freeConnection(database, bd);
            }
            throw excep;
        }
        return i;
    }
