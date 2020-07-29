    public void create() {
        Connection conn = OrmHandler.getInstance().getSession().getConnection(this);
        Statement stat = null;
        StringBuilder sql = new StringBuilder(256);
        try {
            getRenderer().printCreateDatabase(this, sql);
            conn = createConnection();
            stat = conn.createStatement();
            stat.executeUpdate(sql.toString());
            conn.commit();
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info(sql.toString());
            }
        } catch (Throwable e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.WARNING, "Can't rollback DB" + toString(), ex);
                }
            }
            throw new IllegalArgumentException("Statement error:\n" + sql, e);
        } finally {
            try {
                close(conn, stat, null, true);
            } catch (IllegalStateException ex) {
                LOGGER.log(Level.WARNING, "Can't rollback DB" + toString(), ex);
            }
        }
    }
