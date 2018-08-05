    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
            String url = "jdbc:odbc:BearTracConn";
            conn = DriverManager.getConnection(url, "username", "password");
            stmt = conn.createStatement();
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
        }
    }
