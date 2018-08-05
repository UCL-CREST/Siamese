    private void loadDDL() throws IOException {
        try {
            conn.createStatement().executeQuery("SELECT * FROM overrides").close();
        } catch (SQLException e) {
            Statement stmt = null;
            if (!e.getMessage().startsWith(ERR_MISSING_TABLE)) {
                LOG.fatal(SQL_ERROR, e);
                throw new IOException("Error on initial data store read", e);
            }
            String[] qry = { "CREATE TABLE monitor (id INTEGER PRIMARY KEY NOT NULL, status VARCHAR(32) NOT NULL, next_update TIMESTAMP NOT NULL)", "CREATE TABLE overrides (id INT NOT NULL, title VARCHAR(255) NOT NULL, subtitle VARCHAR(255) NOT NULL, enable BOOLEAN NOT NULL DEFAULT TRUE, PRIMARY KEY(id))", "CREATE TABLE settings (var VARCHAR(32) NOT NULL, val VARCHAR(255) NOT NULL, PRIMARY KEY(var))", "INSERT INTO settings (var, val) VALUES ('schema', '1')" };
            try {
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                for (String q : qry) stmt.executeUpdate(q);
                conn.commit();
            } catch (SQLException e2) {
                try {
                    conn.rollback();
                } catch (SQLException e3) {
                    LOG.fatal(SQL_ERROR, e3);
                }
                LOG.fatal(SQL_ERROR, e2);
                throw new IOException("Error initializing data store", e2);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e4) {
                        LOG.fatal(SQL_ERROR, e4);
                        throw new IOException("Unable to cleanup data store resources", e4);
                    }
                }
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e3) {
                    LOG.fatal(SQL_ERROR, e3);
                    throw new IOException("Unable to reset data store auto commit", e3);
                }
            }
        }
        return;
    }
