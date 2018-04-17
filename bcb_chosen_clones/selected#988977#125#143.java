    public void testCommitRollback() throws Exception {
        Statement stmt = con.createStatement();
        assertNotNull(stmt);
        assertTrue(con.getAutoCommit());
        stmt.execute("CREATE TABLE #TESTCOMMIT (id int primary key)");
        con.setAutoCommit(false);
        assertFalse(con.getAutoCommit());
        assertEquals(1, stmt.executeUpdate("INSERT INTO #TESTCOMMIT VALUES (1)"));
        con.commit();
        assertEquals(1, stmt.executeUpdate("INSERT INTO #TESTCOMMIT VALUES (2)"));
        assertEquals(1, stmt.executeUpdate("INSERT INTO #TESTCOMMIT VALUES (3)"));
        con.rollback();
        assertEquals(1, stmt.executeUpdate("INSERT INTO #TESTCOMMIT VALUES (4)"));
        con.setAutoCommit(true);
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM #TESTCOMMIT");
        rs.next();
        assertEquals("commit", 2, rs.getInt(1));
        stmt.close();
    }
