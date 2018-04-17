    public void testSavepoint9() throws Exception {
        Statement stmt = con.createStatement();
        stmt.execute("CREATE TABLE #savepoint9 (data int)");
        stmt.close();
        con.setAutoCommit(false);
        Savepoint sp = con.setSavepoint();
        PreparedStatement pstmt = con.prepareStatement("INSERT INTO #savepoint9 (data) VALUES (?)");
        pstmt.setInt(1, 1);
        assertTrue(pstmt.executeUpdate() == 1);
        pstmt.close();
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT SUM(data) FROM #savepoint9");
        assertTrue(rs.next());
        assertTrue(rs.getInt(1) == 1);
        assertTrue(!rs.next());
        stmt.close();
        rs.close();
        con.commit();
        con.rollback();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT SUM(data) FROM #savepoint9");
        assertTrue(rs.next());
        assertTrue("bug [2021839]", rs.getInt(1) == 1);
        assertTrue(!rs.next());
        stmt.close();
        rs.close();
        con.setAutoCommit(true);
    }
