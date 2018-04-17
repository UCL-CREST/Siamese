    private void load() throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = FidoDataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            clearTables(stmt);
            stmt.executeQuery("select setval('objects_objectid_seq', 1000)");
            stmt.executeQuery("select setval('instructions_instructionid_seq', 1)");
            stmt.executeQuery("select setval('transactions_transactionid_seq', 1)");
            stmt.executeQuery("select setval('verbtransactions_verbid_seq', 1)");
            stmt.executeUpdate("update SystemProperties set value = 'Minimal Data' where name = 'DB Data Version'");
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
