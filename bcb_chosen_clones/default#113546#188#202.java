    private static void testdb() throws Exception {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (Exception e) {
            System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }
        Connection c = DriverManager.getConnection("jdbc:hsqldb:file:data/testdb", "sa", "");
        Statement st = c.createStatement();
        st.execute("insert into test(text, id) values('test', 10);");
        Statement st2 = c.createStatement();
        ResultSet rs = st2.executeQuery("select text from test;");
        if (rs.next()) System.out.println(rs.getString(1));
    }
