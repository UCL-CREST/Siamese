    public static void executeUpdate(Database db, String... statements) throws SQLException {
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection(db);
            con.setAutoCommit(false);
            stmt = con.createStatement();
            for (String statement : statements) {
                stmt.executeUpdate(statement);
            }
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
            }
            throw e;
        } finally {
            closeStatement(stmt);
            closeConnection(con);
        }
    }
