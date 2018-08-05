    public void testSavepoint4() throws Exception {
        Statement stmt = con.createStatement();
        stmt.execute("CREATE TABLE #savepoint4 (data int)");
        stmt.close();
        con.setAutoCommit(false);
        for (int i = 0; i < 3; i++) {
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO #savepoint4 (data) VALUES (?)");
            pstmt.setInt(1, 1);
            assertTrue(pstmt.executeUpdate() == 1);
            Savepoint savepoint = con.setSavepoint();
            assertNotNull(savepoint);
            assertTrue(savepoint.getSavepointId() == 1);
            try {
                savepoint.getSavepointName();
                assertTrue(false);
            } catch (SQLException e) {
            }
            pstmt.setInt(1, 2);
            assertTrue(pstmt.executeUpdate() == 1);
            pstmt.close();
            pstmt = con.prepareStatement("SELECT SUM(data) FROM #savepoint4");
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next());
            assertTrue(rs.getInt(1) == 3);
            assertTrue(!rs.next());
            pstmt.close();
            rs.close();
            con.rollback(savepoint);
            pstmt = con.prepareStatement("SELECT SUM(data) FROM #savepoint4");
            rs = pstmt.executeQuery();
            assertTrue(rs.next());
            assertTrue(rs.getInt(1) == 1);
            assertTrue(!rs.next());
            pstmt.close();
            rs.close();
            con.rollback();
        }
        con.setAutoCommit(true);
    }
