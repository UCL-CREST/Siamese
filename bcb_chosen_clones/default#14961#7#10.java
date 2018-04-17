    public Testdb(String db_file_name_prefix) throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        conn = DriverManager.getConnection("jdbc:hsqldb:" + db_file_name_prefix, "sa", "");
    }
