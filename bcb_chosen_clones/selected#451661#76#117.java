    private synchronized void loadDDL() throws IOException {
        try {
            conn.createStatement().executeQuery("SELECT * FROM non_generic_favs").close();
        } catch (SQLException e) {
            Statement stmt = null;
            if (!e.getMessage().matches(ERR_MISSING_TABLE)) {
                e.printStackTrace(System.out);
                throw new IOException("Error on initial data store read");
            }
            String[] qry = { "CREATE TABLE non_generic_favs (id INT NOT NULL PRIMARY KEY)", "CREATE TABLE ignore_chan_favs (id INT NOT NULL PRIMARY KEY, chanlist LONG VARCHAR)", "CREATE TABLE settings (var VARCHAR(32) NOT NULL, val VARCHAR(255) NOT NULL, PRIMARY KEY(var))", "INSERT INTO settings (var, val) VALUES ('schema', '1')" };
            try {
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                for (String q : qry) stmt.executeUpdate(q);
                conn.commit();
            } catch (SQLException e2) {
                try {
                    conn.rollback();
                } catch (SQLException e3) {
                    e3.printStackTrace(System.out);
                }
                e2.printStackTrace(new PrintWriter(System.out));
                throw new IOException("Error initializing data store");
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e4) {
                        e4.printStackTrace(System.out);
                        throw new IOException("Unable to cleanup data store resources");
                    }
                }
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e3) {
                    e3.printStackTrace(System.out);
                    throw new IOException("Unable to reset data store auto commit");
                }
            }
        }
        return;
    }
