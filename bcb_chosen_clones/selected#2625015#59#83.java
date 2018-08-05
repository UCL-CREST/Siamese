    public void testTransactions() throws Exception {
        con = TestUtil.openDB();
        Statement st;
        ResultSet rs;
        con.setAutoCommit(false);
        assertTrue(!con.getAutoCommit());
        con.setAutoCommit(true);
        assertTrue(con.getAutoCommit());
        st = con.createStatement();
        st.executeUpdate("insert into test_a (imagename,image,id) values ('comttest',1234,5678)");
        con.setAutoCommit(false);
        st.executeUpdate("update test_a set image=9876 where id=5678");
        con.commit();
        rs = st.executeQuery("select image from test_a where id=5678");
        assertTrue(rs.next());
        assertEquals(9876, rs.getInt(1));
        rs.close();
        st.executeUpdate("update test_a set image=1111 where id=5678");
        con.rollback();
        rs = st.executeQuery("select image from test_a where id=5678");
        assertTrue(rs.next());
        assertEquals(9876, rs.getInt(1));
        rs.close();
        TestUtil.closeDB(con);
    }
