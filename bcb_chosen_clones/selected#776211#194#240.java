    public void testPreparedStatement0009() throws Exception {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("create table #t0009 " + "  (i  integer  not null,      " + "   s  char(10) not null)      ");
        con.setAutoCommit(false);
        PreparedStatement pstmt = con.prepareStatement("insert into #t0009 values (?, ?)");
        int rowsToAdd = 8;
        final String theString = "abcdefghijklmnopqrstuvwxyz";
        int count = 0;
        for (int i = 1; i <= rowsToAdd; i++) {
            pstmt.setInt(1, i);
            pstmt.setString(2, theString.substring(0, i));
            count += pstmt.executeUpdate();
        }
        pstmt.close();
        assertEquals(count, rowsToAdd);
        con.rollback();
        ResultSet rs = stmt.executeQuery("select s, i from #t0009");
        assertNotNull(rs);
        count = 0;
        while (rs.next()) {
            count++;
            assertEquals(rs.getString(1).trim().length(), rs.getInt(2));
        }
        assertEquals(count, 0);
        con.commit();
        pstmt = con.prepareStatement("insert into #t0009 values (?, ?)");
        rowsToAdd = 6;
        count = 0;
        for (int i = 1; i <= rowsToAdd; i++) {
            pstmt.setInt(1, i);
            pstmt.setString(2, theString.substring(0, i));
            count += pstmt.executeUpdate();
        }
        assertEquals(count, rowsToAdd);
        con.commit();
        pstmt.close();
        rs = stmt.executeQuery("select s, i from #t0009");
        count = 0;
        while (rs.next()) {
            count++;
            assertEquals(rs.getString(1).trim().length(), rs.getInt(2));
        }
        assertEquals(count, rowsToAdd);
        con.commit();
        stmt.close();
        con.setAutoCommit(true);
    }
