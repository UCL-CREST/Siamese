    public void Connect() throws SQLException {
        String url = "jdbc:odbc:Purchase Orders";
        con = DriverManager.getConnection(url, "Admin", "");
        System.out.println("Trans  isol is: " + con.getTransactionIsolation());
        System.out.println("AutoCommit  is: " + con.getAutoCommit());
    }
