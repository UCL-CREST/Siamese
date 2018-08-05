    public void execUpdate(String sqlStmts[]) throws SQLException {
        if (conn == null || conn.isClosed()) throw new SQLException("The connection has not been established yet.");
        if (sqlStmts == null || sqlStmts.length == 0) throw new SQLException("SQL-statement is null.");
        conn.setAutoCommit(false);
        try {
            for (int i = 0; i < sqlStmts.length; i++) {
                stmt = conn.createStatement();
                stmt.executeUpdate(sqlStmts[i]);
                logger.debug(sqlStmts[i]);
                stmt.close();
            }
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        }
    }
