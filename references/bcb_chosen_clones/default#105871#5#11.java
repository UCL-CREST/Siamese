    public Connection getSQLConnection() throws Exception {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        String url = "jdbc:odbc:bbs";
        Connection con = DriverManager.getConnection(url);
        System.out.println("MySQL ODBC ���Դ���ӳɹ�...");
        return con;
    }
