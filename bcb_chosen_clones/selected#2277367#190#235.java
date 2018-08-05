    public void testPreparedStatement0009() throws Exception {
        Connection cx = getConnection();
        dropTable("#t0009");
        Statement stmt = cx.createStatement();
        stmt.executeUpdate("create table #t0009 " + "  (i  integer  not null,      " + "   s  char(10) not null)      ");
        cx.setAutoCommit(false);
        PreparedStatement pStmt = cx.prepareStatement("insert into #t0009 values (?, ?)");
        int rowsToAdd = 8;
        final String theString = "abcdefghijklmnopqrstuvwxyz";
        int count = 0;
        for (int i = 1; i <= rowsToAdd; i++) {
            pStmt.setInt(1, i);
            pStmt.setString(2, theString.substring(0, i));
            count += pStmt.executeUpdate();
        }
        assertTrue(count == rowsToAdd);
        cx.rollback();
        stmt = cx.createStatement();
        ResultSet rs = stmt.executeQuery("select s, i from #t0009");
        assertNotNull(rs);
        count = 0;
        while (rs.next()) {
            count++;
            assertTrue(rs.getString(1).trim().length() == rs.getInt(2));
        }
        assertTrue(count == 0);
        cx.commit();
        rowsToAdd = 6;
        count = 0;
        for (int i = 1; i <= rowsToAdd; i++) {
            pStmt.setInt(1, i);
            pStmt.setString(2, theString.substring(0, i));
            count += pStmt.executeUpdate();
        }
        assertTrue(count == rowsToAdd);
        cx.commit();
        rs = stmt.executeQuery("select s, i from #t0009");
        count = 0;
        while (rs.next()) {
            count++;
            assertTrue(rs.getString(1).trim().length() == rs.getInt(2));
        }
        assertTrue(count == rowsToAdd);
        cx.commit();
        cx.setAutoCommit(true);
    }
