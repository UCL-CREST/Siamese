    public void testTransactions0010() throws Exception {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("create table #t0010 " + "  (i  integer  not null,      " + "   s  char(10) not null)      ");
        con.setAutoCommit(false);
        PreparedStatement pstmt = con.prepareStatement("insert into #t0010 values (?, ?)");
        int rowsToAdd = 8;
        final String theString = "abcdefghijklmnopqrstuvwxyz";
        int count = 0;
        for (int i = 1; i <= rowsToAdd; i++) {
            pstmt.setInt(1, i);
            pstmt.setString(2, theString.substring(0, i));
            count += pstmt.executeUpdate();
        }
        assertEquals(count, rowsToAdd);
        con.rollback();
        ResultSet rs = stmt.executeQuery("select s, i from #t0010");
        assertNotNull(rs);
        count = 0;
        while (rs.next()) {
            count++;
            assertEquals(rs.getString(1).trim().length(), rs.getInt(2));
        }
        assertEquals(count, 0);
        rowsToAdd = 6;
        for (int j = 1; j <= 2; j++) {
            count = 0;
            for (int i = 1; i <= rowsToAdd; i++) {
                pstmt.setInt(1, i + ((j - 1) * rowsToAdd));
                pstmt.setString(2, theString.substring(0, i));
                count += pstmt.executeUpdate();
            }
            assertEquals(count, rowsToAdd);
            con.commit();
        }
        rs = stmt.executeQuery("select s, i from #t0010");
        count = 0;
        while (rs.next()) {
            count++;
            int i = rs.getInt(2);
            if (i > rowsToAdd) {
                i -= rowsToAdd;
            }
            assertEquals(rs.getString(1).trim().length(), i);
        }
        assertEquals(count, (2 * rowsToAdd));
        stmt.close();
        pstmt.close();
        con.setAutoCommit(true);
    }
