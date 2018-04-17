    private void loadDDL() throws IOException {
        try {
            conn.createStatement().executeQuery("SELECT * FROM reporters").close();
        } catch (SQLException e) {
            Statement stmt = null;
            if (!e.getMessage().matches(ERR_MISSING_TABLE)) {
                String msg = "Error on initial data store read";
                LOG.fatal(msg, e);
                throw new IOException(msg, e);
            }
            String[] qry = { "CREATE TABLE reporters (type LONG VARCHAR NOT NULL, key LONG VARCHAR NOT NULL, data LONG VARCHAR, PRIMARY KEY(type, key))", "CREATE TABLE listeners (event VARCHAR(255) NOT NULL, type LONG VARCHAR NOT NULL, key LONG VARCHAR NOT NULL, PRIMARY KEY(event, type, key))", "CREATE TABLE settings (var VARCHAR(32) NOT NULL, val VARCHAR(255) NOT NULL, PRIMARY KEY(var))", "INSERT INTO settings (var, val) VALUES ('schema', '1')" };
            try {
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                for (String q : qry) {
                    logQry(q);
                    stmt.executeUpdate(q);
                }
                conn.commit();
            } catch (SQLException e2) {
                String msg = "Error initializing data store";
                try {
                    conn.rollback();
                } catch (SQLException e3) {
                    LOG.fatal(msg, e3);
                }
                LOG.fatal(msg, e2);
                throw new IOException(msg);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e4) {
                        String msg = "Unable to cleanup data store resources";
                        LOG.fatal(msg, e4);
                        throw new IOException(msg);
                    }
                }
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e3) {
                    String msg = "Unable to reset data store auto commit";
                    LOG.fatal(msg, e3);
                    throw new IOException(msg);
                }
            }
        }
        return;
    }
