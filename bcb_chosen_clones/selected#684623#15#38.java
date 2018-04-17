    protected static void clearTables() throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = FidoDataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            ClearData.clearTables(stmt);
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (2, '')");
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (3, '')");
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (4, '')");
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (5, '')");
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (6, '')");
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (7, '')");
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (8, '')");
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
