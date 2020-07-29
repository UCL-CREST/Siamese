    public int visitStatement(String statement) throws SQLException {
        mySQLLogger.info(statement);
        if (getConnection() == null) {
            throw new JdbcException("cannot exec: " + statement + ", because 'not connected to database'");
        }
        Statement stmt = getConnection().createStatement();
        try {
            return stmt.executeUpdate(statement);
        } catch (SQLException ex) {
            getConnection().rollback();
            throw ex;
        } finally {
            stmt.close();
        }
    }
