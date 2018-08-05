    public void testAutoCommit() throws Exception {
        Connection con = getConnectionOverrideProperties(new Properties());
        try {
            Statement stmt = con.createStatement();
            assertEquals(0, stmt.executeUpdate("create table #testAutoCommit (i int)"));
            con.setAutoCommit(false);
            assertEquals(1, stmt.executeUpdate("insert into #testAutoCommit (i) values (0)"));
            con.setAutoCommit(false);
            con.rollback();
            assertEquals(1, stmt.executeUpdate("insert into #testAutoCommit (i) values (1)"));
            con.setAutoCommit(true);
            con.setAutoCommit(false);
            con.rollback();
            con.setAutoCommit(true);
            ResultSet rs = stmt.executeQuery("select i from #testAutoCommit");
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1));
            assertFalse(rs.next());
            rs.close();
            stmt.close();
        } finally {
            con.close();
        }
    }
