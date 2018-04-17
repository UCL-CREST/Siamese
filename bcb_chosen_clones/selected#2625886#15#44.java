    protected static void clearTables() throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = FidoDataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            ClearData.clearTables(stmt);
            stmt.executeUpdate("delete from MorphologyTags");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('not')");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('plural')");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('third singular')");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('again')");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('past tense')");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('against')");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('deprive')");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('cause to happen')");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('nounify')");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('someone who believes')");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('belief system of')");
            stmt.executeUpdate("insert into MorphologyTags (TagName) values ('capable of')");
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
