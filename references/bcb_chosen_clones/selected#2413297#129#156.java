    public void testPreparedStatementRollback1() throws Exception {
        Connection localCon = getConnection();
        Statement stmt = localCon.createStatement();
        stmt.execute("CREATE TABLE #psr1 (data BIT)");
        localCon.setAutoCommit(false);
        PreparedStatement pstmt = localCon.prepareStatement("INSERT INTO #psr1 (data) VALUES (?)");
        pstmt.setBoolean(1, true);
        assertEquals(1, pstmt.executeUpdate());
        pstmt.close();
        localCon.rollback();
        ResultSet rs = stmt.executeQuery("SELECT data FROM #psr1");
        assertFalse(rs.next());
        rs.close();
        stmt.close();
        localCon.close();
        try {
            localCon.commit();
            fail("Expecting commit to fail, connection was closed");
        } catch (SQLException ex) {
            assertEquals("HY010", ex.getSQLState());
        }
        try {
            localCon.rollback();
            fail("Expecting rollback to fail, connection was closed");
        } catch (SQLException ex) {
            assertEquals("HY010", ex.getSQLState());
        }
    }
